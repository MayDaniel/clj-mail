(defproject clj-mail "0.1.5"
  :description "Send and receive emails from Clojure."
  :repositories {"java-net" "http://download.java.net/maven/2"}
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [javax.mail/mail "1.4.3"]]
  :dev-dependencies [[lein-clojars "0.5.0"]])
