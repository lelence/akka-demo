package com.maogogo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import javax.net.ssl.SSLContext
import java.security.KeyStore
import java.io.InputStream
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory
import akka.http.scaladsl.HttpsConnectionContext
import java.security.SecureRandom
import akka.http.scaladsl.ConnectionContext

/**
 * 
 * 
 * 
 * keytool -genkey -alias client -keypass 123456 -keyalg RSA -keysize 1024 -validity 365 -storetype PKCS12 -keypass 123456 -storepass 123456 -keystore client.p12
 * 
 * keytool -export -alias client -keystore client.p12 -storetype PKCS12 -keypass 123456 -file client.cer
 * 
 */
object TestHttps extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val password: Array[Char] = "123456".toCharArray // do not store passwords in code, read them from somewhere safe!

  val ks: KeyStore = KeyStore.getInstance("PKCS12")
  val keystore: InputStream = getClass.getClassLoader.getResourceAsStream("client.p12")

  // require(keystore != null, "Keystore required!")
  ks.load(keystore, password)

  val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(ks, password)

  val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
  tmf.init(ks)

  val sslContext: SSLContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, new SecureRandom)
  val https: HttpsConnectionContext = ConnectionContext.https(sslContext)

  val routes: Route = get { complete("Hello world!") }

  Http().bindAndHandle(routes, "127.0.0.1", 9443, connectionContext = https)

}