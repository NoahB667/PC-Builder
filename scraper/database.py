import psycopg2
import os
from dotenv import load_dotenv

load_dotenv()

VALID_COMPONENTS = ['cases', 'cpus', 'gpus', 'motherboards', 'psus', 'rams', 'storages']

def get_db_connection():
    return psycopg2.connect(
        host=os.getenv("DB_HOST"),
        database=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD")
    )


def upsert_part(table_name, part_name, price):
    """Updates the price if part exists, otherwise inserts it."""
    connection = get_db_connection()
    cursor = connection.cursor()

    query = f"""
        INSERT INTO {table_name} (part_name, price, last_updated)
        VALUES (%s, %s, CURRENT_TIMESTAMP)
        ON CONFLICT (part_name) 
        DO UPDATE SET price = EXCLUDED.price, last_updated = CURRENT_TIMESTAMP;
    """

    try:
        cursor.execute(query, (part_name, price))
        connection.commit()
    except Exception as e:
        print(f"Error updating {table_name}: {e}")
        connection.rollback()
    finally:
        cursor.close()
        connection.close()
