SELECT setval(pg_get_serial_sequence('fetch_operations', 'id'), COALESCE(MAX(id) + 1, 1), false)
FROM fetch_operations;
