CREATE TABLE public.users
(
    user_id bigint NOT NULL,
    is_active boolean NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    image_url character varying(255) COLLATE pg_catalog."default",
    is_email_verified boolean NOT NULL,
    last_name character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    provider character varying(255) COLLATE pg_catalog."default",
    provider_id character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;