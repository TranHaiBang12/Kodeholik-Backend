--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-02-21 21:31:06

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 24842)
-- Name: schema_course; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_course;


ALTER SCHEMA schema_course OWNER TO postgres;

--
-- TOC entry 903 (class 1247 OID 24856)
-- Name: chapter_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.chapter_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.chapter_status OWNER TO postgres;

--
-- TOC entry 906 (class 1247 OID 24862)
-- Name: course_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.course_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.course_status OWNER TO postgres;

--
-- TOC entry 909 (class 1247 OID 24868)
-- Name: lesson_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_status AS ENUM (
    'ACTIVATED, INACTIVATED'
);


ALTER TYPE schema_course.lesson_status OWNER TO postgres;

--
-- TOC entry 912 (class 1247 OID 24872)
-- Name: lesson_type; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_type AS ENUM (
    'VIDEO',
    'DOCUMENT',
    'QUIZ',
    'ASSIGNMENT'
);


ALTER TYPE schema_course.lesson_type OWNER TO postgres;

SET default_tablespace = kodeholik_course_data;

SET default_table_access_method = heap;

--
-- TOC entry 228 (class 1259 OID 24976)
-- Name: chapter; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.chapter (
    id integer NOT NULL,
    course_id integer,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    display_order integer,
    status schema_course.chapter_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_course.chapter OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 24981)
-- Name: chapter_id_seq; Type: SEQUENCE; Schema: schema_course; Owner: postgres
--

ALTER TABLE schema_course.chapter ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_course.chapter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 230 (class 1259 OID 24982)
-- Name: course; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.course (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    image character varying(150),
    status schema_course.course_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer,
    rate numeric(3,2),
    number_of_participant integer DEFAULT 0
);


ALTER TABLE schema_course.course OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 24987)
-- Name: course_comment; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.course_comment (
    course_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_course.course_comment OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 24990)
-- Name: course_id_seq; Type: SEQUENCE; Schema: schema_course; Owner: postgres
--

ALTER TABLE schema_course.course ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_course.course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


SET default_tablespace = '';

--
-- TOC entry 271 (class 1259 OID 25473)
-- Name: course_topic; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_topic (
    course_id integer NOT NULL,
    topic_id integer NOT NULL
);


ALTER TABLE schema_course.course_topic OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 25488)
-- Name: course_user; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_user (
    course_id bigint NOT NULL,
    user_id bigint NOT NULL,
    enrolled_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE schema_course.course_user OWNER TO postgres;

SET default_tablespace = kodeholik_course_data;

--
-- TOC entry 233 (class 1259 OID 24991)
-- Name: lesson; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.lesson (
    id integer NOT NULL,
    chapter_id integer,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    display_order integer,
    type schema_course.lesson_type NOT NULL,
    video_url character varying(200),
    attatched_file character varying(250),
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer,
    status schema_course.lesson_status
);


ALTER TABLE schema_course.lesson OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 24996)
-- Name: lesson_id_seq; Type: SEQUENCE; Schema: schema_course; Owner: postgres
--

ALTER TABLE schema_course.lesson ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_course.lesson_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 235 (class 1259 OID 24997)
-- Name: lesson_problem; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.lesson_problem (
    lesson_id integer NOT NULL,
    problem_id integer NOT NULL
);


ALTER TABLE schema_course.lesson_problem OWNER TO postgres;

--
-- TOC entry 5073 (class 0 OID 24976)
-- Dependencies: 228
-- Data for Name: chapter; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5075 (class 0 OID 24982)
-- Dependencies: 230
-- Data for Name: course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (2, 'Introduction to Python', 'Learn the basics of Python', 'python.jpg', 'ACTIVATED', '2025-02-15 10:00:00', 1, '2025-02-15 11:00:00', 1, NULL, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (3, 'Web Development', 'Build websites', 'web.png', 'INACTIVATED', '2025-02-16 14:30:00', 2, '2025-02-16 15:30:00', 2, NULL, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (4, 'Data Science', 'Analyze data', 'data.gif', 'ACTIVATED', '2025-02-17 09:00:00', 3, '2025-02-17 10:00:00', 3, NULL, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (1, 'test1', 'test1', 'test1', 'ACTIVATED', '2025-02-14 23:04:27.938712', 70, '2025-02-14 23:13:10.547928', 70, NULL, 0);


--
-- TOC entry 5076 (class 0 OID 24987)
-- Dependencies: 231
-- Data for Name: course_comment; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5081 (class 0 OID 25473)
-- Dependencies: 271
-- Data for Name: course_topic; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5082 (class 0 OID 25488)
-- Dependencies: 272
-- Data for Name: course_user; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5078 (class 0 OID 24991)
-- Dependencies: 233
-- Data for Name: lesson; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5080 (class 0 OID 24997)
-- Dependencies: 235
-- Data for Name: lesson_problem; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5088 (class 0 OID 0)
-- Dependencies: 229
-- Name: chapter_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.chapter_id_seq', 1, false);


--
-- TOC entry 5089 (class 0 OID 0)
-- Dependencies: 232
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_id_seq', 1, true);


--
-- TOC entry 5090 (class 0 OID 0)
-- Dependencies: 234
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.lesson_id_seq', 1, false);


SET default_tablespace = '';

--
-- TOC entry 4899 (class 2606 OID 25108)
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (id);


--
-- TOC entry 4903 (class 2606 OID 25110)
-- Name: course_comment course_comment_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_pkey PRIMARY KEY (course_id, comment_id);


--
-- TOC entry 4901 (class 2606 OID 25112)
-- Name: course course_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- TOC entry 4909 (class 2606 OID 25477)
-- Name: course_topic course_topic_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT course_topic_pkey PRIMARY KEY (course_id, topic_id);


--
-- TOC entry 4911 (class 2606 OID 25493)
-- Name: course_user course_user_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_pkey PRIMARY KEY (course_id, user_id);


--
-- TOC entry 4905 (class 2606 OID 25114)
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- TOC entry 4907 (class 2606 OID 25116)
-- Name: lesson_problem lesson_problem_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_pkey PRIMARY KEY (lesson_id, problem_id);


--
-- TOC entry 4912 (class 2606 OID 25211)
-- Name: chapter chapter_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 4913 (class 2606 OID 25216)
-- Name: chapter chapter_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4914 (class 2606 OID 25221)
-- Name: chapter chapter_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4917 (class 2606 OID 25226)
-- Name: course_comment course_comment_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 4918 (class 2606 OID 25231)
-- Name: course_comment course_comment_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 4915 (class 2606 OID 25236)
-- Name: course course_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4916 (class 2606 OID 25241)
-- Name: course course_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4926 (class 2606 OID 25494)
-- Name: course_user course_user_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON DELETE CASCADE;


--
-- TOC entry 4927 (class 2606 OID 25499)
-- Name: course_user course_user_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON DELETE CASCADE;


--
-- TOC entry 4924 (class 2606 OID 25478)
-- Name: course_topic fk_course; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON DELETE CASCADE;


--
-- TOC entry 4925 (class 2606 OID 25483)
-- Name: course_topic fk_topic; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT fk_topic FOREIGN KEY (topic_id) REFERENCES schema_setting.topic(id) ON DELETE CASCADE;


--
-- TOC entry 4919 (class 2606 OID 25246)
-- Name: lesson lesson_chapter_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES schema_course.chapter(id);


--
-- TOC entry 4920 (class 2606 OID 25251)
-- Name: lesson lesson_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4922 (class 2606 OID 25256)
-- Name: lesson_problem lesson_problem_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id);


--
-- TOC entry 4923 (class 2606 OID 25261)
-- Name: lesson_problem lesson_problem_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4921 (class 2606 OID 25266)
-- Name: lesson lesson_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


-- Completed on 2025-02-21 21:31:06

--
-- PostgreSQL database dump complete
--

