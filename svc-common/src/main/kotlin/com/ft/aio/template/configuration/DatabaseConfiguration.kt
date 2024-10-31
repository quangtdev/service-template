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
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct
import javax.persistence.*

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.ft.aio.template"],
    considerNestedRepositories = true,
)
@EntityScan(basePackages = ["com.ft.aio.template"])
@PropertySource(factory = YamlPropertySourceFactory::class, value = ["classpath:database-config.yml"])
class DatabaseConfiguration


@Entity(name = "testEntity")
@Table(name = "test_entity", schema = "public")
class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "name")
    val name: String? = null
}

@Repository
interface TestEntityRepository : JpaRepository<TestEntity, Long>

@Component
class TestDatabaseConnection(
    private val testEntityRepository: TestEntityRepository
) {
    private val log by logger()

    @PostConstruct
    fun init() {
        val result = testEntityRepository.findAll()
        log.info(result.map { it.name }.joinToString(","))
    }
}