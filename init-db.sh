#!/bin/bash

DB_NAME="flashcardsdb"
DB_USER="postgres"

DB_EXIST=$(psql -U "$DB_USER" -tAc "SELECT 1 FROM pg_database WHERE datname='$DB_NAME'")

if [ "$DB_EXIST" = "1" ]; then
    echo "La base $DB_NAME existe déjà."
else
    echo "Création de la base $DB_NAME..."
    createdb -U "$DB_USER" "$DB_NAME"
    echo "Base $DB_NAME créée avec succès !"
fi
