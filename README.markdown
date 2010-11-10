### Examples

Wrap a body with `with-session`.

    (with-session "Foo@bar.baz" "MyPassword" "smtp.foobar.baz" 465 "smtp" true
      (send-email (text-email ["YamNad1@gmail.com"] "Subject" "Body")))           

    (with-session "Foo@bar.baz" "MyPassword" "pop.foobar.baz" 995 "pop3s" true
      (folder->records "INBOX"))

### Getting

`:dependencies [[clj-mail "0.1.5"] ...]`

### License

[Eclipse Public License - v 1.0](https://github.com/MayDaniel/clj-mail/blob/master/LICENSE)
