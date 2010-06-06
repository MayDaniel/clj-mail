# Clj-Mail

Send and receive emails from Clojure.

## Usage

### Examples

    (send-msg :host "smtp.gmail.com"
							:port 465 
							:user "Email" 
							:pass "Pass" 
							:ssl true 
							:to ["YamNad1@gmail.com"] 
							:subject "Subject" 
							:body "Body" 
							:type rTO)

    (get-msgs :host "pop.gmail.com" 
              :port 995 
              :user "Email" 
              :pass "Pass" 
              :ssl true 
              :protocol "pop3s" 
              :folder-name "INBOX")

## Installation

- Leiningen.
- Add `[clj-mail "0.1.0"]` to your dependencies in project.clj.

## License

Clj-Mail is licensed under the Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
