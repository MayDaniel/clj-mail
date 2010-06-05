(ns Clj-Mail.core
  (:import (javax.mail Session Message Authenticator PasswordAuthentication Transport Message$RecipientType Folder Flags$Flag)
           (javax.mail.internet MimeMessage InternetAddress)
           (java.util Properties))
  (:use [clojure.contrib.def :only (defnk)]))

(def rTO (Message$RecipientType/TO))
(def rCC (Message$RecipientType/CC))
(def rBCC (Message$RecipientType/BCC))

(defn mk-props [host port user ssl]
  (let [properties (new Properties)]
    (doto properties
      (.put "mail.smtp.host" host)
      (.put "mail.smtp.user" user)
      (.put "mail.smtp.port" port)
      (.put "mail.smtp.socketFactory.port" port)
      (.put "mail.smtp.auth" "true"))
    (when (true? ssl)
      (doto properties
        (.put "mail.smtp.starttls.enable" "true")
        (.put "mail.smtp.socketFactory.class" 
              "javax.net.ssl.SSLSocketFactory")
        (.put "mail.smtp.socketFactory.fallback" "false"))) properties))

(defn get-session [properties user pass]
  (Session/getInstance
   properties 
   (proxy [Authenticator] []
     (getPasswordAuthentication
      []
      (PasswordAuthentication. user pass)))))

(defn mk-msg [session {:keys [to from subject body type]}]
  (let [msg (MimeMessage. session)]
    (doto msg
      (.setFrom (InternetAddress. from))
      (.setSubject subject)
      (.setText body))
    (if (sequential? to)
      (map (fn [s] (.addRecipient msg type (InternetAddress. s))) to)
      (.addRecipient msg type (InternetAddress. to))) msg))

(defn get-store [session host user pass protocol]
  (let [store (.getStore session protocol)]
    (.connect store host user pass) store))

(defn get-folder [store folder-name]
  (let [folder (.getFolder store folder-name)]
    (.open folder Folder/READ_WRITE) folder))

(defn msg-info [msg]
  {:recipients (map str (.getAllRecipients msg))
   :flags (.getFlags msg)
   :folder (str (.getFolder msg))
   :from (str (seq (.getFrom msg)))
   :msg-num (.getMessageNumber msg)
   :sent-date (str (.getSentDate msg))
   :subject (.getSubject msg)
   :content (.getContent msg)})

(defnk send-msg [:host nil :port nil :user nil :pass nil :ssl nil :to nil :subject nil :body nil :type nil]
  (when-not (true? (some nil? [host port user pass to subject body]))
    (let [properties (mk-props host port user ssl)
          session (get-session properties user pass)
          msg (mk-msg session {:to to :from user :subject subject :body body :type type})]
      (Transport/send msg))))

(defnk get-msgs [:host nil :port nil :user nil :pass nil :ssl nil :protocol nil folder-name "INBOX"]
  (let [store (get-store (get-session (Properties.) user pass)  host user pass protocol)
        folder (get-folder store folder-name)]
    (lazy-seq (.getMessages folder))))
