-- =========================
-- Initialisation PROD SAFE
-- PostgreSQL 16
-- =========================

-- Catégories (IDs stables)
INSERT INTO category (id, name) VALUES
(1, 'Bash one-liner'),
(2, 'Kubernetes one-liner'),
(3, 'Git short-liner'),
(4, 'Keytool one-liner'),
(5, 'Vagrant one-liner')
ON CONFLICT (id) DO NOTHING;

-- Flashcards Bash one-liner
INSERT INTO flashcard (question, answer, category_id)
SELECT 'Version of redhat used', 'cat /etc/redhat-release', 1
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Version of redhat used'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'See the File System info',
       'df -h .\n Filesystem      Size  Used Avail Use% Mounted on\nE:      120G   73G   48G  61%   /e',
       1
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'See the File System info'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'See which users is on vm', 'cat /etc/passwd', 1
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'See which users is on vm'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Tar a gz file', 'tar -czvf archive.tar.gz nom_du_fichier.log', 1
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Tar a gz file'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Stop a service',
       'pid=$(ps -ef | grep app_name | grep -v grep | awk ''{print $2}'')\nkill $pid',
       1
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Stop a service'
);

-- Kubernetes
INSERT INTO flashcard (question, answer, category_id)
SELECT 'View pod and node info',
       'kubectl <context_or_namespace> get pods -o wide',
       2
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'View pod and node info'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'View the size of logs', 'ls -lh service_name.log', 2
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'View the size of logs'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'View n lines into the log',
       'kubectl <context_or_namespace> logs pod_name  | grep -C 5 "error"',
       2
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'View n lines into the log'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Use the container name defined into the the Pod or Deployment file',
       'kubectl logs <pod_name> -c <container_name>',
       2
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question LIKE 'Use the container name%'
);

-- Git
INSERT INTO flashcard (question, answer, category_id)
SELECT 'Git configure',
       'git config --global user.name "your Name"\ngit config --global user.email "your.email@example.com"',
       3
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Git configure'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Display the current config', 'git config --list', 3
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Display the current config'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Clone a specific branch', 'git clone -b <branch_name> <repo_url>', 3
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Clone a specific branch'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Create branche and switch on it', 'git checkout -b <branch_name>', 3
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Create branche and switch on it'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Remove a local branch', 'git branch -d <branch_name>', 3
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Remove a local branch'
);

-- Keytool
INSERT INTO flashcard (question, answer, category_id)
SELECT 'Create a Private/Public Key Pair with Keytool',
       'keytool -genkey -alias <alias_name> -keyalg RSA -validity 365 -keystore server.keystore -storetype JKS',
       4
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question LIKE 'Create a Private/Public%'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Generate the certificate',
       'keytool -genkeypair -alias <alias_name> -keypass <keypass> -validity <validity> -storepass <storepass>',
       4
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Generate the certificate'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Listing Certificates in the Keystore',
       'keytool -list -storepass <storepass>',
       4
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Listing Certificates in the Keystore'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Change the alias of a certificate',
       'keytool -changealias -alias <alias> -destalias <new_alias> -keypass <keypass> -storepass <storepass>',
       4
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question LIKE 'Change the alias%'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Add a Certificate to a Truststore',
       'keytool -import -alias <alias_name> -file public.cert -storetype JKS -keystore server.truststore',
       4
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question LIKE 'Add a Certificate%'
);

-- Vagrant
INSERT INTO flashcard (question, answer, category_id)
SELECT 'Initialize a new Vagrant project with the image bionic64 (Ubuntu 18.04)',
       'vagrant init hashicorp/bionic64',
       5
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question LIKE 'Initialize a new Vagrant project%'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Start VM(s)', 'vagrant up', 5
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Start VM(s)'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Stop properly the VM', 'vagrant halt', 5
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Stop properly the VM'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Restart the VM', 'vagrant reload', 5
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Restart the VM'
);

INSERT INTO flashcard (question, answer, category_id)
SELECT 'Remove all the VM(s)', 'vagrant destroy -f', 5
WHERE NOT EXISTS (
    SELECT 1 FROM flashcard WHERE question = 'Remove all the VM(s)'
);
