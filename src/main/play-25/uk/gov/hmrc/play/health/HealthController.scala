/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.health

import javax.inject.Inject
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, Controller}
import play.api.{Configuration, Environment}

class HealthController @Inject()(configuration: Configuration, environment: Environment) extends Controller {

  private lazy val manifest: Manifest = new Manifest() {

    override val env = environment

    lazy val appName = configuration.getString("appName").getOrElse {
      throw new IllegalArgumentException("no config value for key 'appName'")
    }
  }

  def ping = Action {
    Ok
  }

  def details() = Action {
    Ok(toJson(manifest.contents))
  }

  def detail(name: String) = Action {
    manifest.contents.get(name) match {
      case Some(m) => Ok(m)
      case None    => NotFound
    }
  }
}
