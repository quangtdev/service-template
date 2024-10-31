/*******************************************************************************
 * Copyright(c) FriarTuck Pte Ltd ("FriarTuck"). All Rights Reserved.
 *
 * This software is the confidential and proprietary information of FriarTuck.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with FriarTuck.
 *
 * FriarTuck MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-
 * INFRINGEMENT. FriarTuck SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.ft.aio.template.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import org.springframework.lang.Nullable
import org.springframework.util.Assert
import java.io.File
import java.util.*

/**
 * The @PropertySource annotation from Spring framework doesn't work with YAML files
 * out of the box. We need to provide a custom implementation to tell the framework
 * how to translate YAML files into Java Properties.
 *
 */
class YamlPropertySourceFactory : PropertySourceFactory {
    private val log  = LoggerFactory.getLogger(YamlPropertySourceFactory::class.java)

    override fun createPropertySource(@Nullable name: String?, resource: EncodedResource): PropertySource<*> {
        val effectiveProperties = Properties()

        // First, load all properties from the default resource
        val defaultResource = resource.resource as ClassPathResource
        if (defaultResource.exists()) {
            log.info("Activating default configurations: {}.", resource.resource.filename)
            effectiveProperties.putAll(loadYamlIntoProperties(defaultResource)!!)
        }

        /*
         * Then, if config location environment variable is configured, look for customer specific configurations
         * there and override the default values. Otherwise, we will work with profile-specific ClassPathResource
         * within each deployment artifact.
         *
         */if (CONFIG_LOCATION.isPresent) {
            val fileSystemResource: Resource = FileSystemResource(CONFIG_LOCATION.get() + defaultResource.path)
            if (!fileSystemResource.exists()) Assert.isTrue(
                defaultResource.exists(),
                String.format("The YAML file %s does not exist.", defaultResource.path)
            ) else {
                log.info(
                    "Activating externalized configurations at this location: {}.",
                    CONFIG_LOCATION.get() + defaultResource.path
                )
                effectiveProperties.putAll(loadYamlIntoProperties(fileSystemResource)!!)
            }
        } else {
            // If profile-specific properties exist, merge them in and override the default values
            val profileResource = findResourceBasedOnActiveProfiles(resource.resource as ClassPathResource)
            if (profileResource == null) Assert.isTrue(
                defaultResource.exists(),
                String.format("The YAML file %s does not exist.", defaultResource.path)
            ) else {
                log.info("Activating profile-specific configurations: {}.", profileResource.filename)
                effectiveProperties.putAll(loadYamlIntoProperties(profileResource)!!)
            }
        }
        return PropertiesPropertySource(
            (if (name != null && !name.isEmpty()) name else resource.resource.filename)!!,
            effectiveProperties
        )
    }

    private fun loadYamlIntoProperties(resource: Resource): Properties? {
        val factory = YamlPropertiesFactoryBean()
        factory.setResources(resource)
        factory.afterPropertiesSet()
        return factory.getObject()
    }

    private fun findResourceBasedOnActiveProfiles(resource: ClassPathResource): Resource? {
        val resourceName = resource.path
        val activeProfiles = getActiveRuntimeProfiles()
        for (activeProfile in activeProfiles) {
            val resourceNameRelativeToActiveProfile =
                resourceName.replaceFirst("\\.yml".toRegex(), String.format("_%s.yml", activeProfile))

            /*
             * If a resource was indeed created for this active profile, return it.
             * We're making an implicit assumption here that there will NEVER be more
             * than ONE profile-specific resource given a number of active profiles.
             *
             * For example, if the application was started with two active profiles
             * "dev" and "microservice", there can ONLY be either "resource_dev" or
             * "resource_microservice", not both.
             *
             * In other words, we can only categorize configurations in ONE dimension.
             * Otherwise, we need extra logic to decide which configurations should
             * override the other (i.e. which one is of higher priority).
             *
             */
            val potentialResource: Resource = ClassPathResource(resourceNameRelativeToActiveProfile)
            if (potentialResource.exists()) return potentialResource
        }
        return null
    }

    private fun getActiveRuntimeProfiles() : Array<String>{
        val profileConfig = System.getProperty("spring.profiles.active")
        return if (profileConfig == null || profileConfig.isEmpty()) arrayOf() else profileConfig.split(",")
            .toTypedArray()
    }

    companion object {
        private val CONFIG_LOCATION = Optional.ofNullable(System.getenv("WORKFORCE_OPTIMIZER_CONFIG_LOCATION"))
            .map { location: String -> if (location.endsWith(File.separator)) location else location + File.separator }
    }
}