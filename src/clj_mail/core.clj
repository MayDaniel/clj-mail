(ns clj-mail.core
  (:import [javax.mail Session Message Authenticator PasswordAuthentication Transport Message$RecipientType Folder Flags$Flag]
           [javax.mail.internet MimeMessage MimeMultipart InternetAddress MimeBodyPart]
           [javax.activation FileDataSource URLDataSource DataHandler]
           [java.util Properties]
           [java.io File]))

(def rTO (Message$RecipientType/TO))
(def rCC (Message$RecipientType/CC))
(def rBCC (Message$RecipientType/BCC))

(defn mk-session [properties username pass]
  (Session/getInstance
   properties
   (proxy [Authenticator] []
     (getPasswordAuthentication
      []
      (PasswordAuthentication. username
                               pass)))))

(defn mk-props [out-host out-port username ssl?]
  (let [properties (new Properties)]
    (doto properties
      (.put "mail.smtp.host" out-host)
      (.put "mail.smtp.user" username)
      (.put "mail.smtp.port" out-port)
      (.put "mail.smtp.socketFactory.port" out-port)
      (.put "mail.smtp.auth" "true"))
    (when ssl?
      (doto properties
        (.put "mail.smtp.starttls.enable" "true")
        (.put "mail.smtp.socketFactory.class" 
              "javax.net.ssl.SSLSocketFactory")
        (.put "mail.smtp.socketFactory.fallback" "false")))
    properties))

(defrecord Sess [session user-info properties])

(defn mk-Sess [{:keys [username pass ssl?
                       in-host in-protocol in-port
                       out-host out-protocol out-port]
                :as user-info}]
  (let [properties (mk-props out-host out-port username ssl?)]
    (Sess. (mk-session properties username pass)
           user-info
           properties)))

(defmacro with-folder [sess folder fname & body]
  `(let [store# (.getStore (:session ~sess)
                           (-> ~sess :user-info :in-protocol))]
     (.connect store#
               (-> ~sess :user-info :in-host)
               (-> ~sess :user-info :username)
               (-> ~sess :user-info :pass))
     (let [~folder (.getFolder store# ~fname)]
       (.open ~folder Folder/READ_WRITE)
       (let [result# (do ~@body)]
         (.close ~folder false)
         (.close store#)
         result#))))

(defn msg->map [msg]
  {:reply-to (map str (.getReplyTo msg))
   :recipients (map str (.getAllRecipients msg))
   :sent-date (str (.getSentDate msg))
   :subject (.getSubject msg)
   :content (.getContent msg)
   :from (map str (seq (.getFrom msg)))})

(defn text-msg [sess {:keys [rtype to-coll subject body] :or {rtype rTO}}]
  (doto (MimeMessage. (:session sess))
    (.setFrom (InternetAddress. (-> sess :user-info :username)))
    (.setSubject subject)
    (.addRecipients rtype (into-array (map #(InternetAddress. %) to-coll)))
    (.setText body)))

(defn send-msg [sess msg]
  (.saveChanges msg)
  (let [transport (.getTransport (:session sess)
                                 (-> sess :user-info :out-protocol))]
    (doto transport
      (.connect (-> sess :user-info :out-host)
                (-> sess :user-info :username)
                (-> sess :user-info :pass))
      (.sendMessage msg (.getAllRecipients msg))
      (.close))))

(defn get-msgs->maps [sess folder-name]
  (with-folder sess folder folder-name
    (doall (map msg->map (seq (.getMessages folder))))))
