package com.whisk.docker

import java.sql.DriverManager

import scala.concurrent.ExecutionContext
import scala.util.Try

trait DockerPostgresServiceLatest extends DockerKit {
  import scala.concurrent.duration._
  def PostgresAdvertisedPort = 5432
  def PostgresExposedPort = 5444
  val PostgresUser = "postgres"
  val PostgresPassword = "postgres"

  val postgresContainer = DockerContainer("postgres:latest")
    .withPorts((PostgresAdvertisedPort, Some(PostgresExposedPort)))
    .withEnv(s"POSTGRES_USER=$PostgresUser", s"POSTGRES_PASSWORD=$PostgresPassword")
    .withReadyChecker(
      new PostgresReadyChecker(PostgresUser, PostgresPassword, Some(PostgresExposedPort))
        .looped(15, 1.second)
    )

  abstract override def dockerContainers: List[DockerContainer] =
    postgresContainer :: super.dockerContainers
}

