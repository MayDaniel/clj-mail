(defproject clj-mail "0.1.1"
  :description "Send and receive emails from Clojure."
  :repositories {"java-net" "http://download.java.net/maven/2"}
  :dependencies [[org.clojure/clojure "1.2.0-RC1"]
                 [org.clojure/clojure-contrib "1.2.0-RC1"]
                 [javax.mail/mail "1.4.3"]]
  :dev-dependencies [[swank-clojure "1.2.1"]]
  :namespaces [clj-mail.core])
