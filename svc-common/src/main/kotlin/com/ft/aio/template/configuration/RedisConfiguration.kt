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

import com.ft.aio.template.util.LoggerExtension.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Configuration
@PropertySource(factory = YamlPropertySourceFactory::class, value = ["classpath:redis-config.yml"])
@EnableRedisRepositories("com.ft.aio.template")
internal class RedisConfiguration {


    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(connectionFactory)
        template.stringSerializer = StringRedisSerializer()
        template.setDefaultSerializer(StringRedisSerializer())
        return template
    }
}


@Component
class TestRedisConnection(private val redisTemplate: RedisTemplate<String, Any>) {
    private val log by logger()

    @PostConstruct
    fun testConnection() {
        try {
            redisTemplate.opsForValue().set("test", "test-value")
            val testValue = redisTemplate.opsForValue().get("test")
            log.info("Redis connection is successful- $testValue")
        } catch (e: Exception) {
            log.error("Redis connection failed", e)
        }
    }
}



