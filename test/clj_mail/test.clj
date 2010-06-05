(ns clj-mail.core-test
  (:use [clj-mail.core] :reload-all)
  (:use [clojure.test]))

(send-msg
 :host "pop.gmail.com"
 :port 465
 :user "YamNad1@gmail.com"
 :pass "PASSWORD"
 :to "YamNad1@gmail.com"
 :subject "SUBJECT"
 :body "BODY"
 :type rTO
 :ssl true)

(def msgs
     (get-msgs
      :host "pop.gmail.com"
      :port 995
      :user "YamNad1@gmail.com"
      :pass "PASSWORD"
      :protocol "pop3s"
      :folder-name "INBOX"))
