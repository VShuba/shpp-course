DO $$
BEGIN
    -- Отключаем проверки внешних ключей
EXECUTE 'SET CONSTRAINTS ALL DEFERRED';

-- Очистка данных во всех таблицах
EXECUTE (
    SELECT string_agg(format('TRUNCATE TABLE %I.%I RESTART IDENTITY CASCADE', table_schema, table_name), '; ')
    FROM information_schema.tables
    WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
);

-- Включаем проверки внешних ключей
EXECUTE 'SET CONSTRAINTS ALL IMMEDIATE';
END $$;
