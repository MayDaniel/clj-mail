### Examples

Wrap a body with `with-session`.

    (with-session "foo@example.com" "my password" "smtp.example.com" 465 "smtp" true
      (send-email (text-email ["bar@example.com"] "Subject" "Body")))           

    (with-session "foo@example.com" "my-password" "pop.example.com" 995 "pop3s" true
      (folder->records "INBOX"))

### Getting

`:dependencies [[clj-mail "0.1.5"] ...]`

### License

[Eclipse Public License - v 1.0](https://github.com/MayDaniel/clj-mail/blob/master/LICENSE)
