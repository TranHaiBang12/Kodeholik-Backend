--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-02-12 15:49:08

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

DROP DATABASE IF EXISTS kodeholik;
--
-- TOC entry 5203 (class 1262 OID 16517)
-- Name: kodeholik; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE kodeholik WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Vietnamese_Vietnam.1258';


ALTER DATABASE kodeholik OWNER TO postgres;

\connect kodeholik

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
-- TOC entry 10 (class 2615 OID 16536)
-- Name: schema_contest; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_contest;


ALTER SCHEMA schema_contest OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 16535)
-- Name: schema_course; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_course;


ALTER SCHEMA schema_course OWNER TO postgres;

--
-- TOC entry 11 (class 2615 OID 16537)
-- Name: schema_discussion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_discussion;


ALTER SCHEMA schema_discussion OWNER TO postgres;

--
-- TOC entry 8 (class 2615 OID 16534)
-- Name: schema_problem; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_problem;


ALTER SCHEMA schema_problem OWNER TO postgres;

--
-- TOC entry 7 (class 2615 OID 16533)
-- Name: schema_setting; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_setting;


ALTER SCHEMA schema_setting OWNER TO postgres;

--
-- TOC entry 6 (class 2615 OID 16532)
-- Name: schema_user; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_user;


ALTER SCHEMA schema_user OWNER TO postgres;

--
-- TOC entry 971 (class 1247 OID 17257)
-- Name: contest_status; Type: TYPE; Schema: schema_contest; Owner: postgres
--

CREATE TYPE schema_contest.contest_status AS ENUM (
    'not started',
    'in progress',
    'ended'
);


ALTER TYPE schema_contest.contest_status OWNER TO postgres;

--
-- TOC entry 1019 (class 1247 OID 17697)
-- Name: chapter_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.chapter_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.chapter_status OWNER TO postgres;

--
-- TOC entry 1022 (class 1247 OID 17708)
-- Name: course_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.course_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.course_status OWNER TO postgres;

--
-- TOC entry 1025 (class 1247 OID 17720)
-- Name: lesson_type; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_type AS ENUM (
    'VIDEO',
    'TEXT',
    'QUIZ, ASSIGNMENT'
);


ALTER TYPE schema_course.lesson_type OWNER TO postgres;

--
-- TOC entry 992 (class 1247 OID 17382)
-- Name: difficulty; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.difficulty AS ENUM (
    'EASY',
    'MEDIUM',
    'HARD'
);


ALTER TYPE schema_problem.difficulty OWNER TO postgres;

--
-- TOC entry 1001 (class 1247 OID 17534)
-- Name: input_type; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.input_type AS ENUM (
    'CHAR',
    'INT',
    'DOUBLE',
    'LONG',
    'BOOLEAN',
    'LIST',
    'MAP',
    'ARR_INT',
    'ARR_DOUBLE',
    'ARR_OBJECT',
    'STRING',
    'ARR_STRING',
    'OBJECT'
);


ALTER TYPE schema_problem.input_type OWNER TO postgres;

--
-- TOC entry 989 (class 1247 OID 17350)
-- Name: problem_status; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.problem_status AS ENUM (
    'PUBLIC',
    'PRIVATE'
);


ALTER TYPE schema_problem.problem_status OWNER TO postgres;

--
-- TOC entry 1016 (class 1247 OID 17677)
-- Name: level; Type: TYPE; Schema: schema_setting; Owner: postgres
--

CREATE TYPE schema_setting.level AS ENUM (
    'FUNDAMENTAL',
    'INTERMEDIATE',
    'ADVANCED'
);


ALTER TYPE schema_setting.level OWNER TO postgres;

--
-- TOC entry 959 (class 1247 OID 17230)
-- Name: transaction_status; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.transaction_status AS ENUM (
    'success',
    'failed'
);


ALTER TYPE schema_user.transaction_status OWNER TO postgres;

--
-- TOC entry 956 (class 1247 OID 17224)
-- Name: transaction_type; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.transaction_type AS ENUM (
    'topup',
    'withdraw'
);


ALTER TYPE schema_user.transaction_type OWNER TO postgres;

--
-- TOC entry 998 (class 1247 OID 17494)
-- Name: user_role; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.user_role AS ENUM (
    'STUDENT',
    'TEACHER',
    'EXAMINER',
    'ADMIN'
);


ALTER TYPE schema_user.user_role OWNER TO postgres;

--
-- TOC entry 995 (class 1247 OID 17504)
-- Name: user_status; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.user_status AS ENUM (
    'ACTIVATED',
    'NOT_ACTIVATED',
    'BANNED'
);


ALTER TYPE schema_user.user_status OWNER TO postgres;

SET default_tablespace = kodeholik_contest_data;

SET default_table_access_method = heap;

--
-- TOC entry 261 (class 1259 OID 17264)
-- Name: contest; Type: TABLE; Schema: schema_contest; Owner: postgres; Tablespace: kodeholik_contest_data
--

CREATE TABLE schema_contest.contest (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    status schema_contest.contest_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_contest.contest OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 17281)
-- Name: contest_coworker; Type: TABLE; Schema: schema_contest; Owner: postgres; Tablespace: kodeholik_contest_data
--

CREATE TABLE schema_contest.contest_coworker (
    contest_id integer NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE schema_contest.contest_coworker OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 17263)
-- Name: contest_id_seq; Type: SEQUENCE; Schema: schema_contest; Owner: postgres
--

ALTER TABLE schema_contest.contest ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_contest.contest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 263 (class 1259 OID 17296)
-- Name: contest_participant; Type: TABLE; Schema: schema_contest; Owner: postgres; Tablespace: kodeholik_contest_data
--

CREATE TABLE schema_contest.contest_participant (
    contest_id integer NOT NULL,
    user_id integer NOT NULL,
    point double precision
);


ALTER TABLE schema_contest.contest_participant OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 17311)
-- Name: contest_problem_point; Type: TABLE; Schema: schema_contest; Owner: postgres; Tablespace: kodeholik_contest_data
--

CREATE TABLE schema_contest.contest_problem_point (
    problem_id integer NOT NULL,
    contest_id integer NOT NULL,
    point double precision
);


ALTER TABLE schema_contest.contest_problem_point OWNER TO postgres;

SET default_tablespace = kodeholik_course_data;

--
-- TOC entry 254 (class 1259 OID 17154)
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
-- TOC entry 253 (class 1259 OID 17153)
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
-- TOC entry 248 (class 1259 OID 17032)
-- Name: course; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.course (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    image character varying(150),
    price double precision NOT NULL,
    status schema_course.course_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_course.course OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 17605)
-- Name: course_comment; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.course_comment (
    course_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_course.course_comment OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 17031)
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


--
-- TOC entry 256 (class 1259 OID 17186)
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
    updated_by integer
);


ALTER TABLE schema_course.lesson OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 17185)
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
-- TOC entry 257 (class 1259 OID 17208)
-- Name: lesson_problem; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.lesson_problem (
    lesson_id integer NOT NULL,
    problem_id integer NOT NULL
);


ALTER TABLE schema_course.lesson_problem OWNER TO postgres;

SET default_tablespace = kodeholik_discussion_data;

--
-- TOC entry 250 (class 1259 OID 17050)
-- Name: comment; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.comment (
    id integer NOT NULL,
    comment text NOT NULL,
    upvote integer,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer,
    comment_reply integer
);


ALTER TABLE schema_discussion.comment OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 17103)
-- Name: comment_vote; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.comment_vote (
    user_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_discussion.comment_vote OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 17049)
-- Name: discussion_id_seq; Type: SEQUENCE; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE schema_discussion.comment ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_discussion.discussion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


SET default_tablespace = kodeholik_problem_data;

--
-- TOC entry 234 (class 1259 OID 16730)
-- Name: problem; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem (
    id bigint NOT NULL,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    difficulty schema_problem.difficulty NOT NULL,
    acceptance_rate numeric(10,2),
    no_submission integer,
    status schema_problem.problem_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_problem.problem OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 17620)
-- Name: problem_comment; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_comment (
    problem_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_problem.problem_comment OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16729)
-- Name: problem_id_seq; Type: SEQUENCE; Schema: schema_problem; Owner: postgres
--

ALTER TABLE schema_problem.problem ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_problem.problem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 267 (class 1259 OID 17556)
-- Name: problem_input_parameter; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_input_parameter (
    id integer NOT NULL,
    problem_id integer,
    name character varying(50) NOT NULL,
    type schema_problem.input_type NOT NULL
);


ALTER TABLE schema_problem.problem_input_parameter OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 17555)
-- Name: problem_input_parameter_id_seq; Type: SEQUENCE; Schema: schema_problem; Owner: postgres
--

ALTER TABLE schema_problem.problem_input_parameter ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_problem.problem_input_parameter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 237 (class 1259 OID 16777)
-- Name: problem_skill; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_skill (
    problem_id integer NOT NULL,
    skill_id integer NOT NULL
);


ALTER TABLE schema_problem.problem_skill OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 16793)
-- Name: problem_solution; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_solution (
    id integer NOT NULL,
    problem_id integer,
    title character varying(100) NOT NULL,
    text_solution text NOT NULL,
    video_url character varying(250),
    is_problem_implementation bit(1)
);


ALTER TABLE schema_problem.problem_solution OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 17636)
-- Name: problem_solution_comment; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_solution_comment (
    problem_solution_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_problem.problem_solution_comment OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 17327)
-- Name: problem_solution_skill; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_solution_skill (
    problem_solution_id integer NOT NULL,
    skill_id integer NOT NULL
);


ALTER TABLE schema_problem.problem_solution_skill OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 17004)
-- Name: problem_submission; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_submission (
    id integer NOT NULL,
    user_id integer NOT NULL,
    problem_id integer NOT NULL,
    code text NOT NULL,
    language_id integer NOT NULL,
    notes character varying(200),
    execution_time double precision NOT NULL,
    memory_usage double precision NOT NULL,
    created_at timestamp without time zone NOT NULL,
    is_accepted boolean NOT NULL,
    status text,
    input_wrong text
);


ALTER TABLE schema_problem.problem_submission OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 17003)
-- Name: problem_submission_id_seq; Type: SEQUENCE; Schema: schema_problem; Owner: postgres
--

ALTER TABLE schema_problem.problem_submission ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_problem.problem_submission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


SET default_tablespace = '';

--
-- TOC entry 242 (class 1259 OID 16868)
-- Name: problem_template; Type: TABLE; Schema: schema_problem; Owner: postgres
--

CREATE TABLE schema_problem.problem_template (
    id integer NOT NULL,
    problem_id integer,
    language_id integer,
    template_code text NOT NULL,
    function_signature character varying(50) NOT NULL,
    return_type schema_problem.input_type
);


ALTER TABLE schema_problem.problem_template OWNER TO postgres;

SET default_tablespace = kodeholik_problem_data;

--
-- TOC entry 236 (class 1259 OID 16762)
-- Name: problem_topic; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_topic (
    problem_id integer NOT NULL,
    topic_id integer NOT NULL
);


ALTER TABLE schema_problem.problem_topic OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 16792)
-- Name: problemsolution_id_seq; Type: SEQUENCE; Schema: schema_problem; Owner: postgres
--

ALTER TABLE schema_problem.problem_solution ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_problem.problemsolution_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 241 (class 1259 OID 16867)
-- Name: problemtemplate_id_seq; Type: SEQUENCE; Schema: schema_problem; Owner: postgres
--

ALTER TABLE schema_problem.problem_template ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_problem.problemtemplate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 240 (class 1259 OID 16840)
-- Name: solution_code; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.solution_code (
    solution_id integer NOT NULL,
    problem_id integer,
    language_id integer,
    code text NOT NULL
);


ALTER TABLE schema_problem.solution_code OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 17133)
-- Name: solution_vote; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.solution_vote (
    user_id integer NOT NULL,
    solution_id integer NOT NULL
);


ALTER TABLE schema_problem.solution_vote OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 16887)
-- Name: test_case; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.test_case (
    id integer NOT NULL,
    problem_id integer,
    input text NOT NULL,
    expected_output text NOT NULL,
    is_sample boolean
);


ALTER TABLE schema_problem.test_case OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 16886)
-- Name: testcase_id_seq; Type: SEQUENCE; Schema: schema_problem; Owner: postgres
--

ALTER TABLE schema_problem.test_case ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_problem.testcase_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 235 (class 1259 OID 16747)
-- Name: user_favourite; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.user_favourite (
    user_id integer NOT NULL,
    problem_id integer NOT NULL
);


ALTER TABLE schema_problem.user_favourite OWNER TO postgres;

SET default_tablespace = kodeholik_setting_data;

--
-- TOC entry 230 (class 1259 OID 16686)
-- Name: language; Type: TABLE; Schema: schema_setting; Owner: postgres; Tablespace: kodeholik_setting_data
--

CREATE TABLE schema_setting.language (
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_setting.language OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16685)
-- Name: language_id_seq; Type: SEQUENCE; Schema: schema_setting; Owner: postgres
--

ALTER TABLE schema_setting.language ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_setting.language_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 16650)
-- Name: skill; Type: TABLE; Schema: schema_setting; Owner: postgres; Tablespace: kodeholik_setting_data
--

CREATE TABLE schema_setting.skill (
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    level schema_setting.level NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_setting.skill OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16649)
-- Name: skill_id_seq; Type: SEQUENCE; Schema: schema_setting; Owner: postgres
--

ALTER TABLE schema_setting.skill ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_setting.skill_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 16668)
-- Name: topic; Type: TABLE; Schema: schema_setting; Owner: postgres; Tablespace: kodeholik_setting_data
--

CREATE TABLE schema_setting.topic (
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_setting.topic OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16667)
-- Name: topic_id_seq; Type: SEQUENCE; Schema: schema_setting; Owner: postgres
--

ALTER TABLE schema_setting.topic ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_setting.topic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


SET default_tablespace = kodeholik_user_data;

--
-- TOC entry 232 (class 1259 OID 16704)
-- Name: notification; Type: TABLE; Schema: schema_user; Owner: postgres; Tablespace: kodeholik_user_data
--

CREATE TABLE schema_user.notification (
    id integer NOT NULL,
    user_id integer NOT NULL,
    content character varying(300) NOT NULL,
    link character varying(200),
    date timestamp without time zone NOT NULL
);


ALTER TABLE schema_user.notification OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16703)
-- Name: notification_id_seq; Type: SEQUENCE; Schema: schema_user; Owner: postgres
--

ALTER TABLE schema_user.notification ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_user.notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 259 (class 1259 OID 17244)
-- Name: transaction; Type: TABLE; Schema: schema_user; Owner: postgres; Tablespace: kodeholik_user_data
--

CREATE TABLE schema_user.transaction (
    id integer NOT NULL,
    user_id integer,
    amount double precision NOT NULL,
    transaction_type schema_user.transaction_type NOT NULL,
    status schema_user.transaction_status NOT NULL,
    description text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    reference_id character varying(100)
);


ALTER TABLE schema_user.transaction OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 17243)
-- Name: transaction_id_seq; Type: SEQUENCE; Schema: schema_user; Owner: postgres
--

ALTER TABLE schema_user.transaction ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_user.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 16570)
-- Name: users; Type: TABLE; Schema: schema_user; Owner: postgres; Tablespace: kodeholik_user_data
--

CREATE TABLE schema_user.users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    fullname character varying(50) NOT NULL,
    password text NOT NULL,
    email character varying(200) NOT NULL,
    role schema_user.user_role NOT NULL,
    status schema_user.user_status NOT NULL,
    created_date date NOT NULL,
    avatar character varying(200)
);


ALTER TABLE schema_user.users OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16569)
-- Name: users_id_seq; Type: SEQUENCE; Schema: schema_user; Owner: postgres
--

ALTER TABLE schema_user.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_user.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 5188 (class 0 OID 17264)
-- Dependencies: 261
-- Data for Name: contest; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5189 (class 0 OID 17281)
-- Dependencies: 262
-- Data for Name: contest_coworker; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5190 (class 0 OID 17296)
-- Dependencies: 263
-- Data for Name: contest_participant; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5191 (class 0 OID 17311)
-- Dependencies: 264
-- Data for Name: contest_problem_point; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5181 (class 0 OID 17154)
-- Dependencies: 254
-- Data for Name: chapter; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5175 (class 0 OID 17032)
-- Dependencies: 248
-- Data for Name: course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5195 (class 0 OID 17605)
-- Dependencies: 268
-- Data for Name: course_comment; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5183 (class 0 OID 17186)
-- Dependencies: 256
-- Data for Name: lesson; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5184 (class 0 OID 17208)
-- Dependencies: 257
-- Data for Name: lesson_problem; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5177 (class 0 OID 17050)
-- Dependencies: 250
-- Data for Name: comment; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--

INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (1, 'This practice is too good. Recomment you guys should take it', 0, '2025-01-02 12:56:21.718047', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (2, 'Absolutely agree', 0, '2025-01-02 12:56:21.718047', 2, NULL, NULL, 1);
INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (3, 'Yes sir', 0, '2025-01-02 12:56:21.718047', 3, NULL, NULL, 1);
INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (4, 'This problem actually has many ways to solve.', 0, '2025-01-02 12:56:21.718047', 4, NULL, NULL, 1);
INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (5, 'I dont understand the problem, can someone explain it to me?', 0, '2025-01-02 12:56:21.718047', 5, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (6, 'What part do you not understand', 0, '2025-01-01 12:56:21.718047', 6, NULL, NULL, 5);
INSERT INTO schema_discussion.comment (id, comment, upvote, created_at, created_by, updated_at, updated_by, comment_reply) OVERRIDING SYSTEM VALUE VALUES (7, 'Every part', 0, '2025-01-03 12:56:21.718047', 5, NULL, NULL, 5);


--
-- TOC entry 5178 (class 0 OID 17103)
-- Dependencies: 251
-- Data for Name: comment_vote; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--



--
-- TOC entry 5161 (class 0 OID 16730)
-- Dependencies: 234
-- Data for Name: problem; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (25, 'Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.', 'MEDIUM', 51.72, 29, 'PUBLIC', '2025-01-23 22:00:00', 1, '2025-01-24 10:11:02.232449', 1);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (6, 'Two Sum II - Input Array Is Sorted', 'Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.

Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.

The tests are generated such that there is exactly one solution. You may not use the same element twice.

Your solution must use only constant extra space.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:56:21.718047', 1, NULL, 1);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (13, 'Add Two Numbers', 'You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 13:29:35.752301', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (30, 'Distribute Elements Into Two Arrays II', 'You are given a 1-indexed array of integers nums of length n.

We define a function greaterCount such that greaterCount(arr, val) returns the number of elements in arr that are strictly greater than val.

You need to distribute all the elements of nums between two arrays arr1 and arr2 using n operations. In the first operation, append nums[1] to arr1. In the second operation, append nums[2] to arr2. Afterwards, in the ith operation:

If greaterCount(arr1, nums[i]) > greaterCount(arr2, nums[i]), append nums[i] to arr1.
If greaterCount(arr1, nums[i]) < greaterCount(arr2, nums[i]), append nums[i] to arr2.
If greaterCount(arr1, nums[i]) == greaterCount(arr2, nums[i]), append nums[i] to the array with a lesser number of elements.
If there is still a tie, append nums[i] to arr1.
The array result is formed by concatenating the arrays arr1 and arr2. For example, if arr1 == [1,2,3] and arr2 == [4,5,6], then result = [1,2,3,4,5,6].

Return the integer array result.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (24, 'Divide Two Integers', 'Given two integers dividend and divisor, divide two integers without using multiplication, division, and mod operator.

The integer division should truncate toward zero, which means losing its fractional part. For example, 8.345 would be truncated to 8, and -2.7335 would be truncated to -2.

Return the quotient after dividing dividend by divisor.

Note: Assume we are dealing with an environment that could only store integers within the 32-bit signed integer range: [−231, 231 − 1]. For this problem, if the quotient is strictly greater than 231 - 1, then return 231 - 1, and if the quotient is strictly less than -231, then return -231.', 'MEDIUM', 0.00, 0, 'PRIVATE', '2025-01-18 16:25:07.673921', 1, '2025-01-18 16:25:12.583489', 1);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (2, 'Add Two Numbers II', 'You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, 1);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (27, 'Sum of Two Integers', 'Given two integers a and b, return the sum of the two integers without using the operators + and -.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (28, 'Two City Scheduling', 'A company is planning to interview 2n people. Given the array costs where costs[i] = [aCosti, bCosti], the cost of flying the ith person to city a is aCosti, and the cost of flying the ith person to city b is bCosti.

Return the minimum cost to fly every person to a city such that exactly n people arrive in each city.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (29, 'Largest Merge Of Two Strings', 'ou are given two strings word1 and word2. You want to construct a string merge in the following way: while either word1 or word2 are non-empty, choose one of the following options:

If word1 is non-empty, append the first character in word1 to merge and delete it from word1.
For example, if word1 = "abc" and merge = "dv", then after choosing this operation, word1 = "bc" and merge = "dva".
If word2 is non-empty, append the first character in word2 to merge and delete it from word2.
For example, if word2 = "abc" and merge = "", then after choosing this operation, word2 = "bc" and merge = "a".
Return the lexicographically largest merge you can construct.

A string a is lexicographically larger than a string b (of the same length) if in the first position where a and b differ, a has a character strictly larger than the corresponding character in b. For example, "abcd" is lexicographically larger than "abcc" because the first position they differ is at the fourth character, and d is greater than c.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (32, 'Maximum XOR of Two Numbers in an Array', 'Given an integer array nums, return the maximum result of nums[i] XOR nums[j], where 0 <= i <= j < n.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (40, 'Power of Two', 'Given an integer n, return true if it is a power of two. Otherwise, return false.

An integer n is a power of two, if there exists an integer x such that n == 2x.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (31, 'Find Minimum Diameter After Merging Two Trees', 'There exist two undirected trees with n and m nodes, numbered from 0 to n - 1 and from 0 to m - 1, respectively. You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the first tree and edges2[i] = [ui, vi] indicates that there is an edge between nodes ui and vi in the second tree.

You must connect one node from the first tree with another node from the second tree with an edge.

Return the minimum possible diameter of the resulting tree.

The diameter of a tree is the length of the longest path between any two nodes in the tree.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (33, 'Split Two Strings to Make Palindrome', 'You are given two strings a and b of the same length. Choose an index and split both strings at the same index, splitting a into two strings: aprefix and asuffix where a = aprefix + asuffix, and splitting b into two strings: bprefix and bsuffix where b = bprefix + bsuffix. Check if aprefix + bsuffix or bprefix + asuffix forms a palindrome.

When you split a string s into sprefix and ssuffix, either ssuffix or sprefix is allowed to be empty. For example, if s = "abc", then "" + "abc", "a" + "bc", "ab" + "c" , and "abc" + "" are valid splits.

Return true if it is possible to form a palindrome string, otherwise return false.

Notice that x + y denotes the concatenation of strings x and y.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (34, 'Minimum Number of Steps to Make Two Strings Anagram II', 'You are given two strings s and t. In one step, you can append any character to either s or t.

Return the minimum number of steps to make s and t anagrams of each other.

An anagram of a string is a string that contains the same characters with a different (or the same) ordering.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (35, 'Words Within Two Edits of Dictionary', 'You are given two string arrays, queries and dictionary. All words in each array comprise of lowercase English letters and have the same length.

In one edit you can take a word from queries, and change any letter in it to any other letter. Find all words from queries that, after a maximum of two edits, equal some word from dictionary.

Return a list of all words from queries, that match with some word from dictionary after a maximum of two edits. Return the words in the same order they appear in queries.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (36, 'Maximize Win From Two Segments', 'There are some prizes on the X-axis. You are given an integer array prizePositions that is sorted in non-decreasing order, where prizePositions[i] is the position of the ith prize. There could be different prizes at the same position on the line. You are also given an integer k.

You are allowed to select two segments with integer endpoints. The length of each segment must be k. You will collect all prizes whose position falls within at least one of the two selected segments (including the endpoints of the segments). The two selected segments may intersect.

For example if k = 2, you can choose segments [1, 3] and [2, 4], and you will win any prize i that satisfies 1 <= prizePositions[i] <= 3 or 2 <= prizePositions[i] <= 4.
Return the maximum number of prizes you can win if you choose the two segments optimally.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (37, 'Minimum Score by Changing Two Elements', 'You are given an integer array nums.

The low score of nums is the minimum absolute difference between any two integers.
The high score of nums is the maximum absolute difference between any two integers.
The score of nums is the sum of the high and low scores.
Return the minimum score after changing two elements of nums', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (38, 'Longest Non-decreasing Subarray From Two Arrays', 'You are given two 0-indexed integer arrays nums1 and nums2 of length n.

Let''s define another 0-indexed integer array, nums3, of length n. For each index i in the range [0, n - 1], you can assign either nums1[i] or nums2[i] to nums3[i].

Your task is to maximize the length of the longest non-decreasing subarray in nums3 by choosing its values optimally.

Return an integer representing the length of the longest non-decreasing subarray in nums3.

Note: A subarray is a contiguous non-empty sequence of elements within an array.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (39, 'Shift Distance Between Two Strings', 'You are given two strings s and t of the same length, and two integer arrays nextCost and previousCost.

In one operation, you can pick any index i of s, and perform either one of the following actions:

Shift s[i] to the next letter in the alphabet. If s[i] == ''z'', you should replace it with ''a''. This operation costs nextCost[j] where j is the index of s[i] in the alphabet.
Shift s[i] to the previous letter in the alphabet. If s[i] == ''a'', you should replace it with ''z''. This operation costs previousCost[j] where j is the index of s[i] in the alphabet.
The shift distance is the minimum total cost of operations required to transform s into t.

Return the shift distance from s to t.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (41, 'Intersection of Two Arrays', 'Given two integer arrays nums1 and nums2, return an array of their 
intersection
. Each element in the result must be unique and you may return the result in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (42, 'Intersection of Two Arrays II', 'Given two integer arrays nums1 and nums2, return an array of their intersection. Each element in the result must appear as many times as it shows in both arrays and you may return the result in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (43, 'Minimum Index Sum of Two Lists', 'Given two arrays of strings list1 and list2, find the common strings with the least index sum.

A common string is a string that appeared in both list1 and list2.

A common string with the least index sum is a common string such that if it appeared at list1[i] and list2[j] then i + j should be the minimum value among all the other common strings.

Return all the common strings with the least index sum. Return the answer in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (44, 'Number of Days Between Two Dates', 'Write a program to count the number of days between two dates.

The two dates are given as strings, their format is YYYY-MM-DD as shown in the examples.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (45, 'Find the Distance Value Between Two Arrays', 'Given two integer arrays arr1 and arr2, and the integer d, return the distance value between the two arrays.

The distance value is defined as the number of elements arr1[i] such that there is not any element arr2[j] where |arr1[i]-arr2[j]| <= d.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (46, 'Two Out of Three', 'Given three integer arrays nums1, nums2, and nums3, return a distinct array containing all the values that are present in at least two out of the three arrays. You may return the values in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (47, 'Keep Multiplying Found Values by Two', 'You are given an array of integers nums. You are also given an integer original which is the first number that needs to be searched for in nums.

You then do the following steps:

If original is found in nums, multiply it by two (i.e., set original = 2 * original).
Otherwise, stop the process.
Repeat this process with the new number as long as you keep finding the number.
Return the final value of original.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (48, 'Buy Two Chocolates', 'You are given an integer array prices representing the prices of various chocolates in a store. You are also given a single integer money, which represents your initial amount of money.

You must buy exactly two chocolates in such a way that you still have some non-negative leftover money. You would like to minimize the sum of the prices of the two chocolates you buy.

Return the amount of money you will have leftover after buying the two chocolates. If there is no way for you to buy two chocolates without ending up in debt, return money. Note that the leftover must be non-negative.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (49, 'Valid Number', 'Given a string s, return whether s is a valid number.

For example, all the following are valid numbers: "2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789", while the following are not valid numbers: "abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53".

Formally, a valid number is defined using one of the following definitions:

An integer number followed by an optional exponent.
A decimal number followed by an optional exponent.
An integer number is defined with an optional sign ''-'' or ''+'' followed by digits.

A decimal number is defined with an optional sign ''-'' or ''+'' followed by one of the following definitions:

Digits followed by a dot ''.''.
Digits followed by a dot ''.'' followed by digits.
A dot ''.'' followed by digits.
An exponent is defined with an exponent notation ''e'' or ''E'' followed by an integer number.

The digits are defined as one or more digits.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (50, 'Text Justification', 'Given an array of strings words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully (left and right) justified.

You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces '' '' when necessary so that each line has exactly maxWidth characters.

Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line does not divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.

For the last line of text, it should be left-justified, and no extra space is inserted between words.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (51, 'Candy', 'There are n children standing in a line. Each child is assigned a rating value given in the integer array ratings.

You are giving candies to these children subjected to the following requirements:

Each child must have at least one candy.
Children with a higher rating get more candies than their neighbors.
Return the minimum number of candies you need to have to distribute the candies to the children.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL);


--
-- TOC entry 5196 (class 0 OID 17620)
-- Dependencies: 269
-- Data for Name: problem_comment; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 1);
INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 2);
INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 3);
INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 4);
INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 5);
INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 6);
INSERT INTO schema_problem.problem_comment (problem_id, comment_id) VALUES (25, 7);


--
-- TOC entry 5194 (class 0 OID 17556)
-- Dependencies: 267
-- Data for Name: problem_input_parameter; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_input_parameter (id, problem_id, name, type) OVERRIDING SYSTEM VALUE VALUES (1, 25, 'nums', 'ARR_INT');
INSERT INTO schema_problem.problem_input_parameter (id, problem_id, name, type) OVERRIDING SYSTEM VALUE VALUES (2, 25, 'target', 'INT');


--
-- TOC entry 5164 (class 0 OID 16777)
-- Dependencies: 237
-- Data for Name: problem_skill; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_skill (problem_id, skill_id) VALUES (25, 1);
INSERT INTO schema_problem.problem_skill (problem_id, skill_id) VALUES (25, 3);
INSERT INTO schema_problem.problem_skill (problem_id, skill_id) VALUES (25, 4);
INSERT INTO schema_problem.problem_skill (problem_id, skill_id) VALUES (25, 5);


--
-- TOC entry 5166 (class 0 OID 16793)
-- Dependencies: 239
-- Data for Name: problem_solution; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5197 (class 0 OID 17636)
-- Dependencies: 270
-- Data for Name: problem_solution_comment; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5192 (class 0 OID 17327)
-- Dependencies: 265
-- Data for Name: problem_solution_skill; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5173 (class 0 OID 17004)
-- Dependencies: 246
-- Data for Name: problem_submission; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (28, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 5 / 0 ; 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.79, 4, '2025-02-04 16:30:00.313059', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"java.lang.ArithmeticException: / by zero. Error at line: 129. Faulty code: int a \u003d 5 / 0 ;"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (29, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 5 / 0 ; 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-04 16:31:50.954949', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"java.lang.ArithmeticException: / by zero. Error at line: 129. Faulty code: int a \u003d 5 / 0 ;"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (30, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 5 / 0 ; 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.79, 5, '2025-02-04 16:32:46.866728', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"java.lang.ArithmeticException: / by zero. Error at line: 129. Faulty code: int a \u003d 5 / 0 ;"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (31, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 while(true){} ; 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0, 0, '2025-02-04 16:33:16.299249', false, 'Compilation Error:
Main.java:129: error: unreachable statement
 while(true){} ; 
               ^
Main.java:130: error: unreachable statement
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }
    ^
2 errors
', NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (38, 1, 50, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.26, 7, '2025-02-04 16:40:05.203941', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (34, 1, 30, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 6, '2025-02-04 16:35:31.124888', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (36, 1, 31, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 6, '2025-02-04 16:37:20.596617', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (39, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-05 20:11:38.34506', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (40, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 5 / 0; 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.66, 5, '2025-02-05 20:13:23.271574', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"java.lang.ArithmeticException: / by zero. Error at line: 129. Faulty code: int a \u003d 5 / 0;"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (41, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 while(true){}; 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0, 0, '2025-02-05 20:14:14.474401', false, 'Compilation Error:
Main.java:129: error: unreachable statement
 while(true){}; 
              ^
Main.java:130: error: unreachable statement
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }
    ^
2 errors
', NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (42, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 0;
 while(a < 5){} 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.92, 5, '2025-02-05 20:15:26.243532', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"Timeout"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (43, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 0;
 while(a < 5){} 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.33, 5, '2025-02-06 16:26:14.92512', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"Timeout"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (27, 1, 27, 'public static int[] twoSum(int[] nums, int target) {int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 4, '2025-02-04 16:29:20.072421', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (37, 1, 49, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.26, 6, '2025-02-04 16:37:36.669806', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (44, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
 while(a < 5){} 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0, 0, '2025-02-06 16:26:43.138743', false, 'Compilation Error:
Main.java:130: error: cannot find symbol
 while(a < 5){} 
       ^
  symbol:   variable a
  location: class Main
1 error
', NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (45, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-06 16:26:49.171475', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (48, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 5 / 0; 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.79, 5, '2025-02-12 14:10:16.132029', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"java.lang.ArithmeticException: / by zero. Error at line: 129. Faulty code: int a \u003d 5 / 0;"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (49, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{0,1}; }', 1, NULL, 0.52, 5, '2025-02-12 14:11:10.639121', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (50, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{0,1}; }', 1, NULL, 0.52, 5, '2025-02-12 14:12:48.523053', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (51, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{0, 1};}}}return new int[]{0,1}; }', 1, NULL, 0.39, 6, '2025-02-12 14:13:29.550824', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"[0,1]"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (52, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 while(true){} 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0, 0, '2025-02-12 14:14:24.720866', false, 'Compilation Error:
Main.java:131: error: unreachable statement
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }
    ^
1 error
', NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (53, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 0; while(true){a < 5} 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0, 0, '2025-02-12 14:15:06.292512', false, 'Compilation Error:
Main.java:129: error: illegal start of type
 int a = 0; while(true){a < 5} 
                            ^
Main.java:129: error: not a statement
 int a = 0; while(true){a < 5} 
                          ^
2 errors
', NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (54, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 int a = 0; while(a < 5){} 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.39, 6, '2025-02-12 14:15:22.272091', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[3.0,2.0,4.0]},{"name":"target","type":"INT","value":6.0}],"expectedOutput":"[1,2]","status":"Failed","actualOutput":"Timeout"}');
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (55, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 4, '2025-02-12 14:48:48.26606', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (32, 1, 40, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-04 16:33:33.928312', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (33, 1, 41, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-04 16:34:44.260842', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (35, 2, 27, 'public static int[] twoSum(int[] nums, int target) {
 
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 6, '2025-02-04 16:35:45.338065', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (46, 1, 6, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 4, '2025-02-11 23:50:16.606352', true, NULL, NULL);
INSERT INTO schema_problem.problem_submission (id, user_id, problem_id, code, language_id, notes, execution_time, memory_usage, created_at, is_accepted, status, input_wrong) OVERRIDING SYSTEM VALUE VALUES (47, 1, 13, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-12 14:09:19.409465', true, NULL, NULL);


--
-- TOC entry 5169 (class 0 OID 16868)
-- Dependencies: 242
-- Data for Name: problem_template; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_template (id, problem_id, language_id, template_code, function_signature, return_type) OVERRIDING SYSTEM VALUE VALUES (2, 25, 1, 'public int[] twoSum(int[] nums, int target) {}', 'twoSum', 'ARR_INT');
INSERT INTO schema_problem.problem_template (id, problem_id, language_id, template_code, function_signature, return_type) OVERRIDING SYSTEM VALUE VALUES (3, 25, 2, 'int* twoSum(int* nums, int numsSize, int target, int* returnSize) {}', 'twoSum', 'ARR_INT');


--
-- TOC entry 5163 (class 0 OID 16762)
-- Dependencies: 236
-- Data for Name: problem_topic; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_topic (problem_id, topic_id) VALUES (25, 2);
INSERT INTO schema_problem.problem_topic (problem_id, topic_id) VALUES (25, 3);
INSERT INTO schema_problem.problem_topic (problem_id, topic_id) VALUES (25, 9);


--
-- TOC entry 5167 (class 0 OID 16840)
-- Dependencies: 240
-- Data for Name: solution_code; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5179 (class 0 OID 17133)
-- Dependencies: 252
-- Data for Name: solution_vote; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5171 (class 0 OID 16887)
-- Dependencies: 244
-- Data for Name: test_case; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.test_case (id, problem_id, input, expected_output, is_sample) OVERRIDING SYSTEM VALUE VALUES (2, 25, '[
        {
            "name": "nums",
            "type": "ARR_INT",
            "value": [3,2,4]
        },
        {
            "name": "target",
            "type": "INT",
            "value": 6
        }
    ]', '[1,2]', true);
INSERT INTO schema_problem.test_case (id, problem_id, input, expected_output, is_sample) OVERRIDING SYSTEM VALUE VALUES (3, 25, '[
        {
            "name": "nums",
            "type": "ARR_INT",
            "value": [3,3]
        },
        {
            "name": "target",
            "type": "INT",
            "value": 6
        }
    ]', '[0,1]', true);
INSERT INTO schema_problem.test_case (id, problem_id, input, expected_output, is_sample) OVERRIDING SYSTEM VALUE VALUES (4, 25, '[
        {
            "name": "nums",
            "type": "ARR_INT",
            "value": [6,4,5,6]
        },
        {
            "name":"target",
            "type": "INT",
            "value": 12
        }
    ]', '[0,3]', false);
INSERT INTO schema_problem.test_case (id, problem_id, input, expected_output, is_sample) OVERRIDING SYSTEM VALUE VALUES (5, 25, '[
        {
            "name":"nums",
            "type": "ARR_INT",
            "value": [2,9,5,6]
        },
        {
            "name":"target",
            "type": "INT",
            "value": 7
        }
    ]', '[0,2]', false);
INSERT INTO schema_problem.test_case (id, problem_id, input, expected_output, is_sample) OVERRIDING SYSTEM VALUE VALUES (1, 25, '[
        {
            "name": "nums",
            "type": "ARR_INT",
            "value": [2,7,11,15]
        },
        {
            "name": "target",
            "type": "INT",
            "value": 9
        }
    ]', '[0,1]', true);


--
-- TOC entry 5162 (class 0 OID 16747)
-- Dependencies: 235
-- Data for Name: user_favourite; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5157 (class 0 OID 16686)
-- Dependencies: 230
-- Data for Name: language; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--

INSERT INTO schema_setting.language (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (1, 'Java', '2025-01-23 22:00:00', 1, NULL, NULL);
INSERT INTO schema_setting.language (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (2, 'C', '2025-01-23 22:00:00', 1, NULL, NULL);


--
-- TOC entry 5153 (class 0 OID 16650)
-- Dependencies: 226
-- Data for Name: skill; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--

INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (1, 'Array', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (2, 'String', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (3, 'Brute Force', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (4, 'Two-pass Hash Table', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (5, 'One-pass Hash Table', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (6, 'Dynamic Programming', 'ADVANCED', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (7, 'Backtracking', 'ADVANCED', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (8, 'Trie', 'ADVANCED', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (9, 'Hash Table', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (10, 'Depth-First Search', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (11, 'Math', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (12, 'Recursion', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (13, 'Tree', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (14, 'Binary Tree', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (15, 'Greedy', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill (id, name, level, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (16, 'Bredth-First Search', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);


--
-- TOC entry 5155 (class 0 OID 16668)
-- Dependencies: 228
-- Data for Name: topic; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--

INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (1, 'Companies', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (2, 'Array', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (3, 'Loop', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (4, 'Recursion', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (5, 'List', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (6, 'String', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (7, 'Map', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (8, 'Object', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic (id, name, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (9, 'Math', '2025-01-02 12:56:21.718047', 1, NULL, NULL);


--
-- TOC entry 5159 (class 0 OID 16704)
-- Dependencies: 232
-- Data for Name: notification; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--



--
-- TOC entry 5186 (class 0 OID 17244)
-- Dependencies: 259
-- Data for Name: transaction; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--



--
-- TOC entry 5151 (class 0 OID 16570)
-- Dependencies: 224
-- Data for Name: users; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--

INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (22, '27 - Trần Hải Bằng', '27 - Trần Hải Bằng', '$2a$10$VS.SuPBGxhmF6LMckxvnauCcZeRYQ/Jzf4WOuhJM9KS.myVT2oY.C', 'bangthhe170871@fpt.edu.vn', 'STUDENT', 'ACTIVATED', '2025-02-10', 'https://lh3.googleusercontent.com/a/ACg8ocLUGVSI7vY8J1U-dedYf6BNl_pub8sHys0YFcbHGUSToW5ihw=s96-c');
INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (2, 'Phong', 'Pham Duy Phong', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'phongk72tp@gmail.com', 'STUDENT', 'ACTIVATED', '2024-05-01', NULL);
INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (3, 'Thai', 'Pham Hong Thai', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'thaiph@gmail.com', 'STUDENT', 'ACTIVATED', '2024-06-02', NULL);
INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (4, 'Duy', 'Dang Nguyen Quang Duy', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'duydnq123@gmail.com', 'STUDENT', 'ACTIVATED', '2024-07-03', NULL);
INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (5, 'HngThng', 'Dang Hong Thang', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'thangdh1557@gmail.com', 'STUDENT', 'ACTIVATED', '2024-09-06', NULL);
INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (6, 'Jasmine Milk', 'Vu Tuan Hung', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'hungvt321@gmail.com', 'STUDENT', 'BANNED', '2024-10-12', NULL);
INSERT INTO schema_user.users (id, username, fullname, password, email, role, status, created_date, avatar) OVERRIDING SYSTEM VALUE VALUES (1, 'mast', 'Tran Hai Bang', '$2a$10$tdcB/Z64SSN3U5rlz.Daa.oWs53G/XysvtLntJ3SPWBJJOKqfliG6', 'tranhaibang665@gmail.com', 'STUDENT', 'ACTIVATED', '2024-03-20', NULL);


--
-- TOC entry 5204 (class 0 OID 0)
-- Dependencies: 260
-- Name: contest_id_seq; Type: SEQUENCE SET; Schema: schema_contest; Owner: postgres
--

SELECT pg_catalog.setval('schema_contest.contest_id_seq', 1, false);


--
-- TOC entry 5205 (class 0 OID 0)
-- Dependencies: 253
-- Name: chapter_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.chapter_id_seq', 1, false);


--
-- TOC entry 5206 (class 0 OID 0)
-- Dependencies: 247
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_id_seq', 1, false);


--
-- TOC entry 5207 (class 0 OID 0)
-- Dependencies: 255
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.lesson_id_seq', 1, false);


--
-- TOC entry 5208 (class 0 OID 0)
-- Dependencies: 249
-- Name: discussion_id_seq; Type: SEQUENCE SET; Schema: schema_discussion; Owner: postgres
--

SELECT pg_catalog.setval('schema_discussion.discussion_id_seq', 7, true);


--
-- TOC entry 5209 (class 0 OID 0)
-- Dependencies: 233
-- Name: problem_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_id_seq', 51, true);


--
-- TOC entry 5210 (class 0 OID 0)
-- Dependencies: 266
-- Name: problem_input_parameter_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_input_parameter_id_seq', 2, true);


--
-- TOC entry 5211 (class 0 OID 0)
-- Dependencies: 245
-- Name: problem_submission_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_submission_id_seq', 55, true);


--
-- TOC entry 5212 (class 0 OID 0)
-- Dependencies: 238
-- Name: problemsolution_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problemsolution_id_seq', 1, false);


--
-- TOC entry 5213 (class 0 OID 0)
-- Dependencies: 241
-- Name: problemtemplate_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problemtemplate_id_seq', 3, true);


--
-- TOC entry 5214 (class 0 OID 0)
-- Dependencies: 243
-- Name: testcase_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.testcase_id_seq', 5, true);


--
-- TOC entry 5215 (class 0 OID 0)
-- Dependencies: 229
-- Name: language_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.language_id_seq', 19, true);


--
-- TOC entry 5216 (class 0 OID 0)
-- Dependencies: 225
-- Name: skill_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.skill_id_seq', 28, true);


--
-- TOC entry 5217 (class 0 OID 0)
-- Dependencies: 227
-- Name: topic_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.topic_id_seq', 15, true);


--
-- TOC entry 5218 (class 0 OID 0)
-- Dependencies: 231
-- Name: notification_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.notification_id_seq', 1, false);


--
-- TOC entry 5219 (class 0 OID 0)
-- Dependencies: 258
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.transaction_id_seq', 1, false);


--
-- TOC entry 5220 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.users_id_seq', 66, true);


SET default_tablespace = '';

--
-- TOC entry 4930 (class 2606 OID 17285)
-- Name: contest_coworker contest_coworker_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_coworker
    ADD CONSTRAINT contest_coworker_pkey PRIMARY KEY (contest_id, user_id);


--
-- TOC entry 4932 (class 2606 OID 17300)
-- Name: contest_participant contest_participant_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_participant
    ADD CONSTRAINT contest_participant_pkey PRIMARY KEY (contest_id, user_id);


--
-- TOC entry 4928 (class 2606 OID 17270)
-- Name: contest contest_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest
    ADD CONSTRAINT contest_pkey PRIMARY KEY (id);


--
-- TOC entry 4934 (class 2606 OID 17315)
-- Name: contest_problem_point contest_problem_point_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_problem_point
    ADD CONSTRAINT contest_problem_point_pkey PRIMARY KEY (problem_id, contest_id);


--
-- TOC entry 4920 (class 2606 OID 17160)
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (id);


--
-- TOC entry 4940 (class 2606 OID 17609)
-- Name: course_comment course_comment_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_pkey PRIMARY KEY (course_id, comment_id);


--
-- TOC entry 4912 (class 2606 OID 17038)
-- Name: course course_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- TOC entry 4922 (class 2606 OID 17192)
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- TOC entry 4924 (class 2606 OID 17212)
-- Name: lesson_problem lesson_problem_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_pkey PRIMARY KEY (lesson_id, problem_id);


--
-- TOC entry 4916 (class 2606 OID 17107)
-- Name: comment_vote comment_vote_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment_vote
    ADD CONSTRAINT comment_vote_pkey PRIMARY KEY (user_id, comment_id);


--
-- TOC entry 4914 (class 2606 OID 17056)
-- Name: comment discussion_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_pkey PRIMARY KEY (id);


--
-- TOC entry 4942 (class 2606 OID 17624)
-- Name: problem_comment problem_comment_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_comment
    ADD CONSTRAINT problem_comment_pkey PRIMARY KEY (problem_id, comment_id);


--
-- TOC entry 4938 (class 2606 OID 17560)
-- Name: problem_input_parameter problem_input_parameter_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_input_parameter
    ADD CONSTRAINT problem_input_parameter_pkey PRIMARY KEY (id);


--
-- TOC entry 4894 (class 2606 OID 17397)
-- Name: problem problem_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_pkey PRIMARY KEY (id);


--
-- TOC entry 4944 (class 2606 OID 17640)
-- Name: problem_solution_comment problem_solution_comment_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_comment
    ADD CONSTRAINT problem_solution_comment_pkey PRIMARY KEY (problem_solution_id, comment_id);


--
-- TOC entry 4936 (class 2606 OID 17331)
-- Name: problem_solution_skill problem_solution_skill_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_pkey PRIMARY KEY (problem_solution_id, skill_id);


--
-- TOC entry 4910 (class 2606 OID 17010)
-- Name: problem_submission problem_submission_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_pkey PRIMARY KEY (id);


--
-- TOC entry 4900 (class 2606 OID 16781)
-- Name: problem_skill problemskill_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_pkey PRIMARY KEY (problem_id, skill_id);


--
-- TOC entry 4902 (class 2606 OID 16799)
-- Name: problem_solution problemsolution_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT problemsolution_pkey PRIMARY KEY (id);


--
-- TOC entry 4906 (class 2606 OID 16874)
-- Name: problem_template problemtemplate_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_pkey PRIMARY KEY (id);


--
-- TOC entry 4898 (class 2606 OID 16766)
-- Name: problem_topic problemtopic_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_pkey PRIMARY KEY (problem_id, topic_id);


--
-- TOC entry 4918 (class 2606 OID 17137)
-- Name: solution_vote solution_vote_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_pkey PRIMARY KEY (user_id, solution_id);


--
-- TOC entry 4904 (class 2606 OID 16846)
-- Name: solution_code solutioncode_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_pkey PRIMARY KEY (solution_id);


--
-- TOC entry 4908 (class 2606 OID 16893)
-- Name: test_case testcase_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT testcase_pkey PRIMARY KEY (id);


--
-- TOC entry 4896 (class 2606 OID 16751)
-- Name: user_favourite userfavourite_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_pkey PRIMARY KEY (user_id, problem_id);


--
-- TOC entry 4888 (class 2606 OID 16692)
-- Name: language language_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_name_key UNIQUE (name);


--
-- TOC entry 4890 (class 2606 OID 16690)
-- Name: language language_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- TOC entry 4880 (class 2606 OID 16656)
-- Name: skill skill_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_name_key UNIQUE (name);


--
-- TOC entry 4882 (class 2606 OID 16654)
-- Name: skill skill_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_pkey PRIMARY KEY (id);


--
-- TOC entry 4884 (class 2606 OID 16674)
-- Name: topic topic_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_name_key UNIQUE (name);


--
-- TOC entry 4886 (class 2606 OID 16672)
-- Name: topic topic_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- TOC entry 4892 (class 2606 OID 16710)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 4926 (class 2606 OID 17250)
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);


--
-- TOC entry 4874 (class 2606 OID 16578)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4876 (class 2606 OID 16574)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4878 (class 2606 OID 16576)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4990 (class 2606 OID 17286)
-- Name: contest_coworker contest_coworker_contest_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_coworker
    ADD CONSTRAINT contest_coworker_contest_id_fkey FOREIGN KEY (contest_id) REFERENCES schema_contest.contest(id);


--
-- TOC entry 4991 (class 2606 OID 17291)
-- Name: contest_coworker contest_coworker_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_coworker
    ADD CONSTRAINT contest_coworker_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4988 (class 2606 OID 17271)
-- Name: contest contest_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest
    ADD CONSTRAINT contest_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4992 (class 2606 OID 17301)
-- Name: contest_participant contest_participant_contest_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_participant
    ADD CONSTRAINT contest_participant_contest_id_fkey FOREIGN KEY (contest_id) REFERENCES schema_contest.contest(id);


--
-- TOC entry 4993 (class 2606 OID 17306)
-- Name: contest_participant contest_participant_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_participant
    ADD CONSTRAINT contest_participant_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4994 (class 2606 OID 17321)
-- Name: contest_problem_point contest_problem_point_contest_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_problem_point
    ADD CONSTRAINT contest_problem_point_contest_id_fkey FOREIGN KEY (contest_id) REFERENCES schema_contest.contest(id);


--
-- TOC entry 4995 (class 2606 OID 17443)
-- Name: contest_problem_point contest_problem_point_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_problem_point
    ADD CONSTRAINT contest_problem_point_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4989 (class 2606 OID 17276)
-- Name: contest contest_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest
    ADD CONSTRAINT contest_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4979 (class 2606 OID 17161)
-- Name: chapter chapter_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 4980 (class 2606 OID 17166)
-- Name: chapter chapter_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4981 (class 2606 OID 17171)
-- Name: chapter chapter_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4999 (class 2606 OID 17615)
-- Name: course_comment course_comment_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 5000 (class 2606 OID 17610)
-- Name: course_comment course_comment_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 4970 (class 2606 OID 17039)
-- Name: course course_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4971 (class 2606 OID 17044)
-- Name: course course_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4982 (class 2606 OID 17193)
-- Name: lesson lesson_chapter_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES schema_course.chapter(id);


--
-- TOC entry 4983 (class 2606 OID 17198)
-- Name: lesson lesson_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4985 (class 2606 OID 17213)
-- Name: lesson_problem lesson_problem_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id);


--
-- TOC entry 4986 (class 2606 OID 17438)
-- Name: lesson_problem lesson_problem_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4984 (class 2606 OID 17203)
-- Name: lesson lesson_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4975 (class 2606 OID 17113)
-- Name: comment_vote comment_vote_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment_vote
    ADD CONSTRAINT comment_vote_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 4976 (class 2606 OID 17108)
-- Name: comment_vote comment_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment_vote
    ADD CONSTRAINT comment_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4972 (class 2606 OID 17595)
-- Name: comment discussion_comment_reply; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_comment_reply FOREIGN KEY (comment_reply) REFERENCES schema_user.users(id) NOT VALID;


--
-- TOC entry 4973 (class 2606 OID 17062)
-- Name: comment discussion_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4974 (class 2606 OID 17067)
-- Name: comment discussion_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5001 (class 2606 OID 17630)
-- Name: problem_comment problem_comment_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_comment
    ADD CONSTRAINT problem_comment_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 5002 (class 2606 OID 17625)
-- Name: problem_comment problem_comment_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_comment
    ADD CONSTRAINT problem_comment_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4952 (class 2606 OID 16737)
-- Name: problem problem_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4998 (class 2606 OID 17561)
-- Name: problem_input_parameter problem_input_parameter_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_input_parameter
    ADD CONSTRAINT problem_input_parameter_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5003 (class 2606 OID 17646)
-- Name: problem_solution_comment problem_solution_comment_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_comment
    ADD CONSTRAINT problem_solution_comment_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 5004 (class 2606 OID 17641)
-- Name: problem_solution_comment problem_solution_comment_problem_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_comment
    ADD CONSTRAINT problem_solution_comment_problem_solution_id_fkey FOREIGN KEY (problem_solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4996 (class 2606 OID 17332)
-- Name: problem_solution_skill problem_solution_skill_problem_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_problem_solution_id_fkey FOREIGN KEY (problem_solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4997 (class 2606 OID 17337)
-- Name: problem_solution_skill problem_solution_skill_skill_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES schema_setting.skill(id);


--
-- TOC entry 4967 (class 2606 OID 17021)
-- Name: problem_submission problem_submission_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 4968 (class 2606 OID 17433)
-- Name: problem_submission problem_submission_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4969 (class 2606 OID 17011)
-- Name: problem_submission problem_submission_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4953 (class 2606 OID 16742)
-- Name: problem problem_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4958 (class 2606 OID 17408)
-- Name: problem_skill problemskill_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4959 (class 2606 OID 16787)
-- Name: problem_skill problemskill_skill_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES schema_setting.skill(id);


--
-- TOC entry 4960 (class 2606 OID 17413)
-- Name: problem_solution problemsolution_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT problemsolution_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4964 (class 2606 OID 16880)
-- Name: problem_template problemtemplate_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 4965 (class 2606 OID 17423)
-- Name: problem_template problemtemplate_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4956 (class 2606 OID 17403)
-- Name: problem_topic problemtopic_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4957 (class 2606 OID 16772)
-- Name: problem_topic problemtopic_topic_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES schema_setting.topic(id);


--
-- TOC entry 4977 (class 2606 OID 17143)
-- Name: solution_vote solution_vote_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_solution_id_fkey FOREIGN KEY (solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4978 (class 2606 OID 17138)
-- Name: solution_vote solution_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4961 (class 2606 OID 16857)
-- Name: solution_code solutioncode_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 4962 (class 2606 OID 17418)
-- Name: solution_code solutioncode_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4963 (class 2606 OID 16847)
-- Name: solution_code solutioncode_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_solution_id_fkey FOREIGN KEY (solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4966 (class 2606 OID 17428)
-- Name: test_case testcase_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT testcase_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4954 (class 2606 OID 17398)
-- Name: user_favourite userfavourite_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4955 (class 2606 OID 16752)
-- Name: user_favourite userfavourite_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4949 (class 2606 OID 16693)
-- Name: language language_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4950 (class 2606 OID 16698)
-- Name: language language_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4945 (class 2606 OID 16657)
-- Name: skill skill_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4946 (class 2606 OID 16662)
-- Name: skill skill_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4947 (class 2606 OID 16675)
-- Name: topic topic_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4948 (class 2606 OID 16680)
-- Name: topic topic_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4951 (class 2606 OID 16711)
-- Name: notification notification_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4987 (class 2606 OID 17251)
-- Name: transaction transaction_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.transaction
    ADD CONSTRAINT transaction_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


-- Completed on 2025-02-12 15:49:08

--
-- PostgreSQL database dump complete
--

