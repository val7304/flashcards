TRUNCATE TABLE flashcard RESTART IDENTITY CASCADE;
TRUNCATE TABLE category RESTART IDENTITY CASCADE;


CREATE TABLE IF NOT EXISTS category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS flashcard (
    id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    category_id INT REFERENCES category(id)
);


-- Catégories
INSERT INTO category (name) VALUES
('Bash one-liner'),
('Kubernetes one-liner'),
('Git short-liner'),
('Keytool one-liner'),
('Vagrant one-liner');

-- flashcard Bash one-liner (category_id = 1)
INSERT INTO flashcard (question, answer, category_id) VALUES
('Version of redhat used', 'cat /etc/redhat-release', 1),
('See the File System info', 'df -h .\n Filesystem      Size  Used Avail Use% Mounted on\nE:      120G   73G   48G  61%   /e', 1),
('See which users is on vm', 'cat /etc/passwd', 1),
('Tar a gz file', 'tar -czvf archive.tar.gz nom_du_fichier.log', 1),
('Stop a service', 'pid=$(ps -ef | grep app_name | grep -v grep | awk ''{print $2}'')\nkill $pid', 1);

-- flashcard Kubernetes one-liner (category_id = 2)
INSERT INTO flashcard (question, answer, category_id) VALUES
('View pod and node info', 'kubectl <context_or_namespace> get pods -o wide', 2),
('View the size of logs', 'ls -lh service_name.log', 2),
('View n lines into the log', 'kubectl <context_or_namespace> logs pod_name  | grep -C 5 "error"', 2),
('Use the container name defined into the the Pod or Deployment file', 'kubectl logs <pod_name> -c <container_name>', 2);

-- flashcard Git short-liner (category_id = 3)
INSERT INTO flashcard (question, answer, category_id) VALUES
('Git configure', 'git config --global user.name "your Name"\ngit config --global user.email "your.email@example.com"', 3),
('Display the current config', 'git config --list', 3),
('Clone a specific branch', 'git clone -b <branch_name> <repo_url>', 3),
('Create branche and switch on it', 'git checkout -b <branch_name>', 3),
('Remove a local branch', 'git branch -d <branch_name>', 3);

-- flashcard Keytool one-liner (category_id = 4)
INSERT INTO flashcard (question, answer, category_id) VALUES
('Create a Private/Public Key Pair with Keytool', 'keytool -genkey -alias <alias_name> -keyalg RSA -validity 365 -keystore server.keystore -storetype JKS', 4),
('Generate the certificate', 'keytool -genkeypair -alias <alias_name> -keypass <keypass> -validity <validity> -storepass <storepass>', 4),
('Listing Certificates in the Keystore', 'keytool -list -storepass <storepass>', 4),
('Change the alias of a certificate', 'keytool -changealias -alias <alias> -destalias <new_alias> -keypass <keypass> -storepass <storepass>', 4),
('Add a Certificate to a Truststore', 'keytool -import -alias <alias_name> -file public.cert -storetype JKS -keystore server.truststore', 4);

-- flashcard Vagrant one-liner (category_id = 5)
INSERT INTO flashcard (question, answer, category_id) VALUES
('Initialize a new Vagrant project with the image bionic64 (Ubuntu 18.04)', 'vagrant init hashicorp/bionic64', 5),
('Start VM(s)', 'vagrant up', 5),
('Stop properly the VM', 'vagrant halt', 5),
('Restart the VM', 'vagrant reload', 5),
('Remove all the VM(s)', 'vagrant destroy -f', 5);
