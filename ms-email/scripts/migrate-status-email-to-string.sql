BEGIN;

ALTER TABLE public.tb_emails
    DROP CONSTRAINT tb_emails_status_email_check;

ALTER TABLE public.tb_emails
    ALTER COLUMN status_email TYPE varchar(255)
    USING CASE status_email
        WHEN 0 THEN 'SENT'
        WHEN 1 THEN 'ERROR'
        ELSE status_email::text
    END;

ALTER TABLE public.tb_emails
    ADD CONSTRAINT tb_emails_status_email_check
    CHECK (status_email IN ('SENT', 'ERROR'));

COMMIT;
