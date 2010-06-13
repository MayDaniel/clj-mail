# Clj-Mail

Send and receive emails from Clojure.

## Usage

At the moment, `send-msg` only supports text-only emails.

### Examples

    (send-msg :host "smtp.gmail.com"
	          :port 465
	          :user "YamNad1@gmail.com"
	          :pass "password"
	          :ssl true
	          :to ["YamNad1@gmail.com"]
	          :subject "Subject"
	          :body "Body"
	          :type rTO)

    (get-msgs :host "pop.gmail.com"
              :port 995
              :user "YamNad1@gmail.com"
              :pass "password"
              :ssl true
              :protocol "pop3s"
              :folder-name "INBOX")

## Installation

- Leiningen.
- Add `[clj-mail "0.1.0"]` to your dependencies in project.clj.

## License

Clj-Mail is licensed under the Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
