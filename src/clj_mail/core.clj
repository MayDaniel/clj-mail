(ns clj-mail.core
  (:import [javax.mail Session Message Authenticator PasswordAuthentication Transport Message$RecipientType Folder Flags$Flag]
           [javax.mail.internet MimeMessage InternetAddress]
           [java.util Properties]))

(declare *session*)

(def rTO (Message$RecipientType/TO))
(def rCC (Message$RecipientType/CC))
(def rBCC (Message$RecipientType/BCC))

(defn properties [username host port ssl]
  (let [properties (Properties.)]
    (doto properties
      (.put "mail.smtp.host" host)
      (.put "mail.smtp.user" username)
      (.put "mail.smtp.port" port)
      (.put "mail.smtp.socketFactory.port" port)
      (.put "mail.smtp.auth" "true"))
  (when ssl
    (doto properties
      (.put "mail.smtp.starttls.enable" "true")
      (.put "mail.smtp.socketFactory.class" "javax.net.ssl.SSLSocketFactory")
      (.put "mail.smtp.socketFactory.fallback" "false")))
  properties))

(defn session [properties username password]
  (Session/getInstance properties
    (proxy [Authenticator] []
      (getPasswordAuthentication []
        (PasswordAuthentication. username password)))))

(defn open-store [session]
  (let [{:keys [session protocol host username password]} session]
    (doto (.getStore session protocol)
      (.connect host username password))))

(defn open-folder [folder store]
  (doto (.getFolder store folder)
    (.open Folder/READ_WRITE)))

(defn text-email [recipients subject text]
  (let [{:keys [session username]} *session*]
    (doto (MimeMessage. session)
      (.setFrom (InternetAddress. username))
      (.setRecipients rTO (into-array (map #(InternetAddress. %) recipients)))
      (.setSubject subject)
      (.setText text))))

(defn send-email [email]
  (Transport/send email))

(defmacro with-folder [name folder-name & body]
  `(let [store#  (open-store *session*)
         ~name   (open-folder ~folder-name store#)
         result# (do ~@body)]
     (.close ~name false)
     (.close store#)
     result#))

(defrecord Email [subject content sent received from recipients reply-to])

(defn process-email [email]
  (Email.
   (.getSubject email)
   (.getContent email)
   (.getSentDate email)
   (.getReceivedDate email)
   (map str (.getFrom email))
   (map str (.getAllRecipients email))
   (map str (.getReplyTo email))))

(defn folder->records [folder-name]
  (with-folder folder folder-name
    (doall (map process-email (.getMessages folder)))))

(defmacro with-session [username password host port protocol ssl & body]
  (let [properties (list 'properties username host port ssl)
        session    (list 'session properties username password)]
    `(binding [*session* {:session ~session :properties ~properties
                          :username ~username :password ~password
                          :ssl ~ssl :host ~host :port ~port :protocol ~protocol}]
       ~@body)))
