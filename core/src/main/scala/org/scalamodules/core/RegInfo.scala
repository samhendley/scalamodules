/**
 * Copyright 2009 Heiko Seeberger and others.
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
package org.scalamodules.core

import internal.Util.toOption

import scala.collection.Map
import scala.collection.immutable.{Map => IMap}

/**
 * Companion object for RegIndepInfo providing implicit conversions.
 */
object RegIndepInfo {

  /**
   * Implicitly converts the given object to RegIndepInfo.
   */
  implicit def toRegIndepInfo[S <: AnyRef](srv: S) = new RegIndepInfo(srv) 
}

/**
 * Registration information for an independent service.
 */
class RegIndepInfo[I <: AnyRef, S <: I](val srv: S,
                                        val srvIntf: Option[Class[I]],
                                        val props: Option[Map[String, Any]]) {

  require(srv != null, "Service to be registered must not be null!")
  require(srvIntf != null, "Option for service interface used for registration must not be null!")
  require(props != null, "Option for service properties must not be null!")

  def this(srv: S) = this(srv, None, None)

  def this(srv: S, srvIntf: Option[Class[I]]) = this(srv, srvIntf, None)

  /**
   * Register a service under the given service interface.
   */
  def as(srvIntf: Class[I]) = new RegIndepInfo(srv, srvIntf, props) 

  /**
   * Register a service with the given properties.
   */
  def withProps(props: Map[String, Any]) = 
    new RegIndepInfo(srv, srvIntf, props) 

  /**
   * Register a service with the given properties.
   */
  def withProps(props: (String, Any)*) = 
    if (props.isEmpty)
      new RegIndepInfo(srv, srvIntf, null)
    else 
      new RegIndepInfo(srv, srvIntf, IMap[String, Any](props: _*))
}

/**
 * Companion object for RegInfo providing implicit conversions.
 */
object RegDepInfo {

  /**
   * Implicitly converts the given function to RegDepInfo.
   */
  implicit def toRegDepInfo[S <: AnyRef, D](srv: D => S) = new RegDepInfo(srv) 
}

/**
 * Registration information for a service depending on another service.
 */
class RegDepInfo[I <: AnyRef, S <: I, D](val srv: D => S,
                                         val srvIntf: Option[Class[I]],
                                         val props: Option[Map[String, Any]],
                                         val depIntf: Option[Class[D]]) {

  require(srv != null, "Factory function for service to be registered must not be null!")
  require(srvIntf != null, "Option for service interface used for registration must not be null!")
  require(props != null, "Option for service properties must not be null!")
  require(depIntf != null, "Option for interface of the service depending on must not be null!")

  def this(srv: D => S) = this(srv, None, None, None)

  /**
   * Register a service under the given service interface.
   */
  def as(srvIntf: Class[I]) = 
    new RegDepInfo(srv, srvIntf, props, depIntf) 

  /**
   * Register a service with the given properties.
   */
  def withProps(props: Map[String, Any]) = 
    new RegDepInfo(srv, srvIntf, props, depIntf) 

  /**
   * Register a service with the given properties.
   */
  def withProps(props: (String, Any)*) = 
    new RegDepInfo(srv, srvIntf, IMap[String, Any](props: _*), depIntf) 

  /**
   * Register a service depending on a service with the given service interface.
   */
  def dependOn(depIntf: Class[D]) =
    new RegDepInfo(srv, srvIntf, props, depIntf)
}
