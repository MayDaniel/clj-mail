# Clj-Mail

Send and receive emails from Clojure.

## Usage

At the moment, multi-part emails aren't supported.

### Examples

For those who preferred the previous design, use `simple-get` and `simple-send`, or revert to version `0.1.0`.

    (def sess (mk-Sess {:username "YamNad1@gmail.com"
                        :pass "Password"
                        :ssl? true
                        :in-host "pop.gmail.com"
                        :in-protocol "pop3s"
                        :in-port 995
                        :out-host "smtp.gmail.com"
                        :out-protocol "smtp"
                        :out-port 465})

    (send-msg sess (text-msg {:to-coll ["YamNad1@gmail.com"]
                              :subject "Subject"
                              :body "Body"}))

    (get-msgs->maps sess "INBOX)

## Installation

- Add `[clj-mail "0.1.1"]` to your dependencies.

## License

Clj-Mail is licensed under the Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
