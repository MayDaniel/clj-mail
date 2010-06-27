(defproject clj-mail "0.1.0"
  :description "Send and receive emails from Clojure."
  :repositories {"java-net" "http://download.java.net/maven/2"}
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
                 [javax.mail/mail "1.4.3"]]
  :dev-dependencies [[swank-clojure "1.2.1"]
                     [lein-clojars "0.5.0"]]
  :namespaces :all)
