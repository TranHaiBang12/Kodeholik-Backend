--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-03-17 00:16:40

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
-- TOC entry 5280 (class 1262 OID 16517)
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
-- TOC entry 11 (class 2615 OID 18769)
-- Name: schema_course; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_course;


ALTER SCHEMA schema_course OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 16537)
-- Name: schema_discussion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_discussion;


ALTER SCHEMA schema_discussion OWNER TO postgres;

--
-- TOC entry 10 (class 2615 OID 16536)
-- Name: schema_exam; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_exam;


ALTER SCHEMA schema_exam OWNER TO postgres;

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
-- TOC entry 1032 (class 1247 OID 18771)
-- Name: chapter_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.chapter_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.chapter_status OWNER TO postgres;

--
-- TOC entry 1035 (class 1247 OID 18776)
-- Name: course_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.course_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.course_status OWNER TO postgres;

--
-- TOC entry 1038 (class 1247 OID 18782)
-- Name: lesson_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED',
    'IN_PROGRESS'
);


ALTER TYPE schema_course.lesson_status OWNER TO postgres;

--
-- TOC entry 1041 (class 1247 OID 18790)
-- Name: lesson_type; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_type AS ENUM (
    'VIDEO',
    'DOCUMENT',
    'QUIZ',
    'ASSIGNMENT',
    'LAB'
);


ALTER TYPE schema_course.lesson_type OWNER TO postgres;

--
-- TOC entry 906 (class 1247 OID 18631)
-- Name: exam_status; Type: TYPE; Schema: schema_exam; Owner: postgres
--

CREATE TYPE schema_exam.exam_status AS ENUM (
    'NOT_STARTED',
    'IN_PROGRESS',
    'END'
);


ALTER TYPE schema_exam.exam_status OWNER TO postgres;

--
-- TOC entry 990 (class 1247 OID 17382)
-- Name: difficulty; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.difficulty AS ENUM (
    'EASY',
    'MEDIUM',
    'HARD'
);


ALTER TYPE schema_problem.difficulty OWNER TO postgres;

--
-- TOC entry 1008 (class 1247 OID 17534)
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
    'OBJECT',
    'SET'
);


ALTER TYPE schema_problem.input_type OWNER TO postgres;

--
-- TOC entry 984 (class 1247 OID 17350)
-- Name: problem_status; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.problem_status AS ENUM (
    'PUBLIC',
    'PRIVATE'
);


ALTER TYPE schema_problem.problem_status OWNER TO postgres;

--
-- TOC entry 1020 (class 1247 OID 18584)
-- Name: submission_status; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.submission_status AS ENUM (
    'SUCCESS',
    'FAILED'
);


ALTER TYPE schema_problem.submission_status OWNER TO postgres;

--
-- TOC entry 1029 (class 1247 OID 17677)
-- Name: level; Type: TYPE; Schema: schema_setting; Owner: postgres
--

CREATE TYPE schema_setting.level AS ENUM (
    'FUNDAMENTAL',
    'INTERMEDIATE',
    'ADVANCED'
);


ALTER TYPE schema_setting.level OWNER TO postgres;

--
-- TOC entry 1023 (class 1247 OID 18591)
-- Name: notification_type; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.notification_type AS ENUM (
    'USER',
    'SYSTEM'
);


ALTER TYPE schema_user.notification_type OWNER TO postgres;

--
-- TOC entry 969 (class 1247 OID 17230)
-- Name: transaction_status; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.transaction_status AS ENUM (
    'success',
    'failed'
);


ALTER TYPE schema_user.transaction_status OWNER TO postgres;

--
-- TOC entry 966 (class 1247 OID 17224)
-- Name: transaction_type; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.transaction_type AS ENUM (
    'topup',
    'withdraw'
);


ALTER TYPE schema_user.transaction_type OWNER TO postgres;

--
-- TOC entry 1005 (class 1247 OID 17494)
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
-- TOC entry 1002 (class 1247 OID 17504)
-- Name: user_status; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.user_status AS ENUM (
    'ACTIVATED',
    'NOT_ACTIVATED',
    'BANNED'
);


ALTER TYPE schema_user.user_status OWNER TO postgres;

SET default_tablespace = kodeholik_course_data;

SET default_table_access_method = heap;

--
-- TOC entry 265 (class 1259 OID 18801)
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
-- TOC entry 266 (class 1259 OID 18806)
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
-- TOC entry 267 (class 1259 OID 18807)
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
-- TOC entry 268 (class 1259 OID 18813)
-- Name: course_comment; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.course_comment (
    course_id integer NOT NULL,
    comment_id integer NOT NULL,
    no_upvote integer,
    created_at timestamp without time zone,
    created_by integer,
    updated_at timestamp without time zone,
    updated_by integer,
    no_comment integer
);


ALTER TABLE schema_course.course_comment OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 18816)
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
-- TOC entry 270 (class 1259 OID 18817)
-- Name: course_rating; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_rating (
    id integer NOT NULL,
    course_id integer NOT NULL,
    user_id integer NOT NULL,
    rating integer,
    comment text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT course_rating_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


ALTER TABLE schema_course.course_rating OWNER TO postgres;

--
-- TOC entry 271 (class 1259 OID 18825)
-- Name: course_rating_id_seq; Type: SEQUENCE; Schema: schema_course; Owner: postgres
--

ALTER TABLE schema_course.course_rating ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_course.course_rating_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 272 (class 1259 OID 18826)
-- Name: course_topic; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_topic (
    course_id integer NOT NULL,
    topic_id integer NOT NULL
);


ALTER TABLE schema_course.course_topic OWNER TO postgres;

--
-- TOC entry 273 (class 1259 OID 18829)
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
-- TOC entry 274 (class 1259 OID 18833)
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
    attached_file character varying(250),
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer,
    status schema_course.lesson_status
);


ALTER TABLE schema_course.lesson OWNER TO postgres;

--
-- TOC entry 275 (class 1259 OID 18838)
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
-- TOC entry 276 (class 1259 OID 18839)
-- Name: lesson_problem; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.lesson_problem (
    lesson_id integer NOT NULL,
    problem_id integer NOT NULL
);


ALTER TABLE schema_course.lesson_problem OWNER TO postgres;

SET default_tablespace = '';

--
-- TOC entry 277 (class 1259 OID 18842)
-- Name: user_lesson_progress; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.user_lesson_progress (
    user_id bigint NOT NULL,
    lesson_id bigint NOT NULL
);


ALTER TABLE schema_course.user_lesson_progress OWNER TO postgres;

SET default_tablespace = kodeholik_discussion_data;

--
-- TOC entry 248 (class 1259 OID 17050)
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
-- TOC entry 249 (class 1259 OID 17103)
-- Name: comment_vote; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.comment_vote (
    user_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_discussion.comment_vote OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 17049)
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


SET default_tablespace = kodeholik_exam_data;

--
-- TOC entry 260 (class 1259 OID 18648)
-- Name: exam; Type: TABLE; Schema: schema_exam; Owner: postgres; Tablespace: kodeholik_exam_data
--

CREATE TABLE schema_exam.exam (
    id bigint NOT NULL,
    code text NOT NULL,
    title character varying(50) NOT NULL,
    description text NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    status schema_exam.exam_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer,
    no_participant integer
);


ALTER TABLE schema_exam.exam OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 18647)
-- Name: exam_id_seq; Type: SEQUENCE; Schema: schema_exam; Owner: postgres
--

ALTER TABLE schema_exam.exam ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_exam.exam_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 264 (class 1259 OID 18723)
-- Name: exam_language_support; Type: TABLE; Schema: schema_exam; Owner: postgres; Tablespace: kodeholik_exam_data
--

CREATE TABLE schema_exam.exam_language_support (
    exam_id integer NOT NULL,
    language_id integer NOT NULL
);


ALTER TABLE schema_exam.exam_language_support OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 18672)
-- Name: exam_participant; Type: TABLE; Schema: schema_exam; Owner: postgres; Tablespace: kodeholik_exam_data
--

CREATE TABLE schema_exam.exam_participant (
    exam_id bigint NOT NULL,
    participant_id integer NOT NULL,
    grade double precision NOT NULL
);


ALTER TABLE schema_exam.exam_participant OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 18688)
-- Name: exam_problem; Type: TABLE; Schema: schema_exam; Owner: postgres; Tablespace: kodeholik_exam_data
--

CREATE TABLE schema_exam.exam_problem (
    exam_id bigint NOT NULL,
    problem_id integer NOT NULL,
    point double precision NOT NULL
);


ALTER TABLE schema_exam.exam_problem OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 18703)
-- Name: exam_submission; Type: TABLE; Schema: schema_exam; Owner: postgres; Tablespace: kodeholik_exam_data
--

CREATE TABLE schema_exam.exam_submission (
    exam_id bigint NOT NULL,
    participant_id integer NOT NULL,
    problem_id integer NOT NULL,
    submission_id integer,
    point double precision NOT NULL
);


ALTER TABLE schema_exam.exam_submission OWNER TO postgres;

SET default_tablespace = kodeholik_problem_data;

--
-- TOC entry 258 (class 1259 OID 18615)
-- Name: language_support; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.language_support (
    problem_id integer NOT NULL,
    language_id integer NOT NULL
);


ALTER TABLE schema_problem.language_support OWNER TO postgres;

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
    updated_by integer,
    is_active boolean,
    link character varying(100)
);


ALTER TABLE schema_problem.problem OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 17620)
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
-- TOC entry 255 (class 1259 OID 17556)
-- Name: problem_input_parameter; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_input_parameter (
    id integer NOT NULL,
    problem_id integer NOT NULL,
    parameters json,
    language_id integer
);


ALTER TABLE schema_problem.problem_input_parameter OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 17555)
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
    is_problem_implementation boolean,
    no_upvote integer,
    created_at timestamp without time zone,
    created_by integer,
    updated_at timestamp without time zone,
    updated_by integer,
    no_comment integer
);


ALTER TABLE schema_problem.problem_solution OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 17636)
-- Name: problem_solution_comment; Type: TABLE; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

CREATE TABLE schema_problem.problem_solution_comment (
    problem_solution_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_problem.problem_solution_comment OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 17327)
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
    message text,
    input_wrong text,
    no_testcase_passed integer,
    status schema_problem.submission_status
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
    language_id integer NOT NULL,
    code text NOT NULL,
    submission_id integer
);


ALTER TABLE schema_problem.solution_code OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 17133)
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
    is_sample boolean,
    language_id integer
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
    date timestamp without time zone NOT NULL,
    type schema_user.notification_type NOT NULL
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
-- TOC entry 252 (class 1259 OID 17244)
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
-- TOC entry 251 (class 1259 OID 17243)
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
-- TOC entry 5262 (class 0 OID 18801)
-- Dependencies: 265
-- Data for Name: chapter; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (1, 1, 'Chương 1: Giới thiệu khóa học', 'Nội dung giới thiệu tổng quan về khóa học', 1, 'ACTIVATED', '2023-10-27 10:00:00', 1, '2023-10-27 11:00:00', 1);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (3, 2, 'Chương 2: Các khái niệm cơ bản', 'Các khái niệm cơ bản', 1, 'ACTIVATED', '2023-10-27 10:00:00', 1, '2023-10-27 11:00:00', 1);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (4, 1, 'Chương 2: Các khái niệm cơ bản', 'Các khái niệm cơ bản', 1, 'ACTIVATED', '2023-10-27 10:00:00', 1, '2023-10-27 11:00:00', 1);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (5, 1, 'thunghiem', 'A complete guide to learn Spring Boot', 1, 'ACTIVATED', '2025-02-28 17:25:29.131767', 1, NULL, NULL);


--
-- TOC entry 5264 (class 0 OID 18807)
-- Dependencies: 267
-- Data for Name: course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (3, 'Web Development', 'Build websites', 'web.png', 'INACTIVATED', '2025-02-16 14:30:00', 2, '2025-02-16 15:30:00', 2, 3.00, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (1, 'thunghiem', 'A complete guide to learn Spring Boot', 'https://example.com/spring-boot-course.jpg', 'ACTIVATED', '2025-02-14 23:04:27.938712', 70, '2025-02-26 20:58:09.965', 1, 2.00, 1);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (5, 'Introduction to Java', 'A complete guide to learn Spring Boot', 'https://example.com/spring-boot-course.jpg', 'ACTIVATED', '2025-02-26 19:43:53.237', 1, '2025-02-26 21:10:09.312', 1, 0.00, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (6, '', '', 'https://example.com/spring-boot-course.jpg', 'ACTIVATED', '2025-02-26 21:24:42.695', 1, '2025-02-26 21:24:42.695', 1, 0.00, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (8, 'Spring Boot Advanced - Updated', 'Updated description', NULL, 'ACTIVATED', '2025-02-28 20:57:41.903', 1, '2025-02-28 20:57:41.903', 1, 0.00, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (9, 'Java Algorithm ', 'Updated description', 'kodeholik-course-image-8969206e-b226-4264-afc0-55d709e3398f', 'ACTIVATED', '2025-02-28 21:01:05.145', 1, '2025-02-28 21:01:05.145', 1, 2.00, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (4, 'Data Science', 'Analyze data', 'data.gif', 'ACTIVATED', '2025-02-17 09:00:00', 3, '2025-02-17 10:00:00', 3, 1.00, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (7, 'Spring Boot Advanced - Updated', 'Updated description', 'https://kodeholik.s3.ap-southeast-1.amazonaws.com/courses/8ad2b872-6d25-4727-ac0a-c5e2e0caf326-temp6.jpg', 'ACTIVATED', '2025-02-28 15:58:43.504', 1, '2025-02-28 15:58:43.504', 1, 3.50, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (2, 'Introduction to Python', 'Learn the basics of Python', 'python.jpg', 'ACTIVATED', '2025-02-15 10:00:00', 1, '2025-02-15 11:00:00', 1, 5.00, 1);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (10, 'aádfasdfasdfasdfasdf', 'aádfasdfasdfasd', NULL, 'ACTIVATED', '2025-03-13 21:07:10.076', 1, '2025-03-13 21:07:10.076', 1, 0.00, 0);


--
-- TOC entry 5265 (class 0 OID 18813)
-- Dependencies: 268
-- Data for Name: course_comment; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_comment VALUES (1, 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 2, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (2, 2, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 8, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 9, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 10, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 11, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 12, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO schema_course.course_comment VALUES (1, 13, NULL, NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 5267 (class 0 OID 18817)
-- Dependencies: 270
-- Data for Name: course_rating; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (1, 3, 1, 2, 'This course is amazing! Do not recommended.', '2025-02-25 17:31:31.986', '2025-03-13 13:48:35.581');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (3, 9, 1, 2, 'This course is amazing! Do not recommended.', '2025-03-13 17:02:45.266', '2025-03-13 17:02:45.266');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (4, 4, 1, 1, 'This course is amazing! Do not recommended.', '2025-03-13 17:10:14.969', '2025-03-13 17:10:14.969');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (5, 2, 1, 5, 'This course is amazing! Do not recommended.', '2025-03-13 17:10:36.624', '2025-03-13 17:10:36.624');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (2, 7, 2, 2, 'This course is amazing! Do not recommended.', '2025-03-13 17:02:29.341', '2025-03-13 17:02:29.341');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (6, 7, 1, 5, 'This course is amazing! Do not recommended.', '2025-03-13 17:11:13.157', '2025-03-13 17:11:13.157');


--
-- TOC entry 5269 (class 0 OID 18826)
-- Dependencies: 272
-- Data for Name: course_topic; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_topic VALUES (2, 3);
INSERT INTO schema_course.course_topic VALUES (3, 2);
INSERT INTO schema_course.course_topic VALUES (1, 1);
INSERT INTO schema_course.course_topic VALUES (1, 3);
INSERT INTO schema_course.course_topic VALUES (1, 2);
INSERT INTO schema_course.course_topic VALUES (5, 1);
INSERT INTO schema_course.course_topic VALUES (5, 3);
INSERT INTO schema_course.course_topic VALUES (5, 2);
INSERT INTO schema_course.course_topic VALUES (6, 1);
INSERT INTO schema_course.course_topic VALUES (7, 1);
INSERT INTO schema_course.course_topic VALUES (7, 2);
INSERT INTO schema_course.course_topic VALUES (7, 3);
INSERT INTO schema_course.course_topic VALUES (8, 1);
INSERT INTO schema_course.course_topic VALUES (8, 2);
INSERT INTO schema_course.course_topic VALUES (8, 3);
INSERT INTO schema_course.course_topic VALUES (9, 1);
INSERT INTO schema_course.course_topic VALUES (9, 2);
INSERT INTO schema_course.course_topic VALUES (9, 3);
INSERT INTO schema_course.course_topic VALUES (10, 2);


--
-- TOC entry 5270 (class 0 OID 18829)
-- Dependencies: 273
-- Data for Name: course_user; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_user VALUES (2, 1, '2025-03-13 20:27:01.198');


--
-- TOC entry 5271 (class 0 OID 18833)
-- Dependencies: 274
-- Data for Name: lesson; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (3, 1, 'Introduction to Python', 'Learn the basics of Python programming.', 1, 'VIDEO', 'https://example.com/python_intro.mp4', 'python_intro.pdf', '2025-02-24 17:58:34.579752', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (5, 1, 'Variables and Data Types', 'Explore different data types in Python.', 1, 'VIDEO', NULL, 'python_variables.zip', '2025-02-24 17:58:46.359087', 2, '2025-02-24 17:58:46.359087', 2, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (6, 1, 'Advanced Python Concepts', 'This lesson covers advanced topics in Python, including object-oriented programming, data structures, algorithms, and more.  It delves into best practices, performance optimization, and common pitfalls.  We will also explore the use of third-party libraries for scientific computing and web development.  Finally, we will touch upon debugging techniques and testing methodologies.', 3, 'VIDEO', NULL, NULL, '2025-02-24 17:58:57.799003', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (7, 1, 'Co ban ve Java', 'Cac thong tin co ban ve Java', 1, 'DOCUMENT', NULL, 'lessons/fd0a2445-117c-40ef-a477-26e9af45c65c-application.txt', '2025-03-01 02:02:25.68546', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (8, 1, 'Co ban ve Array', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1741103933139_TestVideo755.mp4', 'lessons/f70b897d-1371-4450-a30e-f61144196d4d-New Text Document.txt', '2025-03-04 22:58:53.125029', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (9, 1, 'Co ban ve Array', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'lesson/TestVideo86.mp4', 'lessons/09f442c1-658c-4706-bdfb-e5465863d4dd-New Text Document.txt', '2025-03-04 23:18:17.990093', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (10, 1, 'Co ban ve Array', 'Cac thong tin co ban ve Array', 1, 'VIDEO', NULL, NULL, '2025-03-06 21:45:30.440027', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (11, 1, 'Co ban ve Array', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1741272488224_kodeholik – Bucket details – Cloud Storage – My First Project – Google Cloud console and 3 more pages - Personal - Microsoft​ Edge 2025-03-06 21-47-37.mp4', NULL, '2025-03-06 21:48:08.220177', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (12, 1, 'Co ban ve Array', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1741779125623_TestVideo755.mp4', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@7c1780af', '2025-03-12 18:23:17.830363', 1, '2025-03-12 18:32:03.86668', 1, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (13, 1, 'Co ban ve Array AsyncTest', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'uploading...', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@3cf342e9', '2025-03-13 19:39:16.043278', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (14, 1, 'Co ban ve Array AsyncTest', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'uploading...', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@47b03a7e', '2025-03-13 20:09:51.832988', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (15, 1, 'Co ban ve Array AsyncTest3', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'error: failed to upload', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@5123c168', '2025-03-13 20:18:47.674379', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (16, 1, 'Co ban ve Array AsyncTest3', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1741872034695_TestVideo755.mp4', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@325f0fa', '2025-03-13 20:20:33.659626', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (17, 1, 'Co ban ve Array AsyncTest4', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1741872532103_TestVideo755.mp4', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@573b5921', '2025-03-13 20:28:49.932615', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (18, 1, 'Co ban ve Array AsyncTest5', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1741873498149_TestVideo755.mp4', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@45ca4a09', '2025-03-13 20:44:56.956715', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (19, 1, 'Co ban ve Array AsyncTest5', 'Cac thong tin co ban ve Array', 1, 'DOCUMENT', NULL, 'lessons/aeb04e15-a1e8-49a0-be11-764dfe9185f7-application.txt', '2025-03-13 20:46:25.0857', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (20, 1, 'Thu nghiem document lesson', 'lesson description', 1, 'DOCUMENT', NULL, 'lessons/3065572c-7fe0-4e5f-a4e0-606fb4e5010e-application.txt', '2025-03-15 17:56:17.309374', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (21, 1, 'Thu nghiem document lesson', 'lesson description', 1, 'DOCUMENT', NULL, 'lessons/d9d612f7-a017-4c28-9498-0fda708c6cce-application.txt', '2025-03-15 18:23:42.673566', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (22, 1, 'Co ban ve Array AsyncTest5', 'Cac thong tin co ban ve Array', 1, 'LAB', NULL, 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@22d09927', '2025-03-15 19:41:44.31923', 1, NULL, NULL, NULL);
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (23, 3, 'Thu nghiem yotube lesson', 'lesson description', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:08:18.633023', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (24, 3, 'Thu nghiem yotube lesson', 'desctipton', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:09:12.861883', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (25, 3, 'Thu nghiem video lesson', 'desctipton', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:11:13.128078', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (26, 1, 'Thu nghiem yotube lesson', 'abc', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:28:54.429826', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (27, 1, 'Co ban ve Array AsyncTest5', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1742045568928_TestVideo755.mp4', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@2e632cbd', '2025-03-15 20:32:47.972555', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (28, 1, 'Thu nghiem yotube lesson', 'abc', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:38:05.514777', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (29, 1, 'Thu nghiem yotube lesson', 'abc', 1, 'DOCUMENT', NULL, 'lessons/31ca3ff2-d9d7-4637-9599-7da76738a8a3-application.txt', '2025-03-15 20:38:50.764842', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (30, 1, 'Thu nghiem yotube lesson', 'abc', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:46:48.322535', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (31, 1, 'Co ban ve Array AsyncTest5', 'Cac thong tin co ban ve Array', 1, 'VIDEO', 'videos/1742046470659_TestVideo755.mp4', 'org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@3bf0da53', '2025-03-15 20:47:49.039728', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (32, 1, 'Thu nghiem yotube lesson', 'abc', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:50:22.044219', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (33, 1, 'Thu nghiem yotube lesson', 'asjdbas', 1, 'VIDEO', NULL, NULL, '2025-03-15 20:53:13.853481', 1, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (34, 1, 'Thu nghiem yotube lesson', 'asjdbas', 1, 'VIDEO', 'videos/1742047234056_Create Lesson - Kodeholik and 5 more pages - Personal - Microsoft​ Edge 2025-03-15 20-49-12.mp4', NULL, '2025-03-15 21:00:34.047319', 1, NULL, NULL, 'IN_PROGRESS');


--
-- TOC entry 5273 (class 0 OID 18839)
-- Dependencies: 276
-- Data for Name: lesson_problem; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.lesson_problem VALUES (22, 41);
INSERT INTO schema_course.lesson_problem VALUES (22, 42);
INSERT INTO schema_course.lesson_problem VALUES (22, 43);


--
-- TOC entry 5274 (class 0 OID 18842)
-- Dependencies: 277
-- Data for Name: user_lesson_progress; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.user_lesson_progress VALUES (1, 5);


--
-- TOC entry 5245 (class 0 OID 17050)
-- Dependencies: 248
-- Data for Name: comment; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--

INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (1, 'This practice is too good. Recomment you guys should take it', 0, '2025-01-02 12:56:21.718047', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (2, 'Absolutely agree', 0, '2025-01-02 12:56:21.718047', 2, NULL, NULL, 1);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (3, 'Yes sir', 0, '2025-01-02 12:56:21.718047', 3, NULL, NULL, 1);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (5, 'I dont understand the problem, can someone explain it to me?', 0, '2025-01-02 12:56:21.718047', 5, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (6, 'What part do you not understand', 0, '2025-01-01 12:56:21.718047', 6, NULL, NULL, 5);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (7, 'Every part', 0, '2025-01-03 12:56:21.718047', 5, NULL, NULL, 5);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (8, 'This editorial is so good', 0, '2025-02-02 12:56:21.718047', 2, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (9, 'Please do not post any solution at this section', 0, '2025-02-02 12:56:21.718047', 5, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (10, 'Hehe', 0, '2025-02-02 12:56:21.718047', 3, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (11, 'If this has image to illustrate, it would have been so much better', 0, '2025-02-02 12:56:21.718047', 4, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (12, 'This site is really good, helping me a lot in software engineering', 0, '2025-02-21 19:15:55.427151', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (13, 'This site is really good, helping me a lot in software engineering', 0, '2025-02-21 19:15:59.354771', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (14, 'This problem is a bit hard to understand', 0, '2025-02-21 19:16:19.75001', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (15, 'Where ?', 0, '2025-02-21 19:17:35.226774', 1, NULL, NULL, 14);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (16, 'Say what''s up ?', 0, '2025-02-21 19:18:54.705701', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (17, 'Really easy to understand', 0, '2025-02-21 19:21:38.313679', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (19, 'First comment', 0, '2025-02-21 19:21:57.063229', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (20, 'Fourth comment', 0, '2025-02-21 19:25:46.557622', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (21, 'Fifth comment', 0, '2025-02-21 21:36:19.054252', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (22, 'First comment', 0, '2025-02-21 22:15:56.461285', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (42, 'Hehe', 0, '2025-03-14 21:26:58.826746', 22, NULL, NULL, 35);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (40, 'Say what''s up', 2, '2025-03-14 18:27:59.653442', 84, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (57, 'jjjj', 0, '2025-03-15 19:09:56.490519', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (58, 'aAA', 0, '2025-03-15 19:10:42.337292', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (59, 'bbbbb', 0, '2025-03-15 19:10:56.745145', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (60, 'ccccccccccccccccccc', 0, '2025-03-15 19:11:39.835418', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (61, 'sáasa', 0, '2025-03-15 19:11:43.420014', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (23, 'Sorry, actually third comment', 3, '2025-02-21 22:16:04.051995', 1, '2025-02-21 22:30:43.575884', 1, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (4, 'This problem actually has many ways to solve.', 1, '2025-01-02 12:56:21.718047', 4, NULL, NULL, 1);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (24, 'Sixth comment', 0, '2025-03-03 22:46:02.348017', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (25, 'Sixth comment', 0, '2025-03-03 22:49:13.902679', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (26, 'Sixth comment', 0, '2025-03-03 22:57:17.602091', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (27, 'Seventh comment', 0, '2025-03-03 22:57:22.765554', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (28, 'Ahaha', 0, '2025-03-03 22:57:28.809864', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (29, 'Please gúy, dont post any solution in this section. It is only used for any questions about the title', 0, '2025-03-03 22:58:49.800098', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (33, 'hehe', 0, '2025-03-12 21:36:10.246996', 2, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (41, 'Baby luot di ngang tryna say what''s up', 0, '2025-03-14 18:28:23.402279', 84, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (62, 'ddddddđ', 0, '2025-03-15 19:12:09.107608', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (64, 'cccccccccccccc', 0, '2025-03-15 19:12:44.645307', 1, NULL, NULL, 63);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (63, 'eeeeeeeeeeeeee', 1, '2025-03-15 19:12:38.66282', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (36, 'Yosadadsa', 0, '2025-03-14 14:57:33.192349', 1, '2025-03-14 22:30:14.490976', 1, 35);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (65, 'mmmmmmmmmmmmmm', 0, '2025-03-15 19:14:23.062941', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (66, 'sssssssssssss', 0, '2025-03-15 19:14:26.740844', 1, NULL, NULL, 65);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (67, 'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', 0, '2025-03-15 19:15:27.405644', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (35, 'sadad1', 2, '2025-03-14 14:57:21.318402', 1, '2025-03-15 16:48:52.326146', 1, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (68, 'zzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaa', 0, '2025-03-15 19:15:31.99644', 1, NULL, NULL, 67);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (38, 'Third comment', 0, '2025-03-14 18:26:33.246693', 3, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (69, 'zzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaa', 0, '2025-03-15 19:15:33.273486', 1, NULL, NULL, 67);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (70, 'zzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaa', 0, '2025-03-15 19:15:34.920956', 1, NULL, NULL, 67);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (71, 'zzzzzzzzzzzzzzzzzzzzaaaaaaaaaaaa', 0, '2025-03-15 19:15:35.634329', 1, NULL, NULL, 67);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (46, '1223', 1, '2025-03-15 18:44:29.102762', 1, '2025-03-15 18:44:43.73206', 1, 45);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (45, 'Ehehe12', 0, '2025-03-15 18:42:30.083926', 1, '2025-03-15 18:44:41.040281', 1, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (47, '1213131', 1, '2025-03-15 18:47:58.345991', 1, NULL, NULL, 41);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (43, 'asda23', 1, '2025-03-15 16:47:39.340247', 1, '2025-03-15 18:48:19.961498', 1, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (49, '232', 0, '2025-03-15 18:56:48.28785', 1, NULL, NULL, 34);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (73, 's', 0, '2025-03-15 19:16:08.428482', 1, NULL, NULL, 72);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (39, 'Yes sir', 0, '2025-03-14 18:27:01.637828', 1, NULL, NULL, 37);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (37, 'Please do not post any solution at this section', 3, '2025-03-14 18:25:59.883216', 5, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (72, 'nnnnnnnnnnn', 1, '2025-03-15 19:16:05.334854', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (48, 'ss', 1, '2025-03-15 18:56:45.025475', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (34, 'hehe', 0, '2025-03-12 22:08:44.163074', 2, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (32, 'hehe', 1, '2025-03-07 19:06:57.043751', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (31, 'saada', 1, '2025-03-07 07:09:35.573509', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (51, '12', 1, '2025-03-15 19:02:20.383563', 1, NULL, NULL, 50);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (50, 'sssss', 0, '2025-03-15 19:02:05.995332', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (52, 'aaa', 0, '2025-03-15 19:03:14.357661', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (53, '3232', 0, '2025-03-15 19:03:24.968403', 1, NULL, NULL, 52);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (44, 'sadadas', 1, '2025-03-15 16:49:14.8905', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (30, 'Please gúy, dont post any solution in this section. It is only used for any questions about the title', 1, '2025-03-03 22:59:25.193033', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (54, 'ppppppppppp', 0, '2025-03-15 19:09:25.252182', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (55, 'sadadas', 0, '2025-03-15 19:09:28.307697', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (56, 'aaaaaaaaaaaaaaaaaaaa', 0, '2025-03-15 19:09:45.421874', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (76, 'oooooooo', 1, '2025-03-15 19:17:34.468173', 1, NULL, NULL, 75);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (74, 'gggggggggggg', 0, '2025-03-15 19:16:24.69101', 1, NULL, NULL, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (77, 'Say what''s upsss', 1, '2025-03-15 19:18:00.397616', 1, '2025-03-15 19:18:22.135748', 1, NULL);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (78, 'ádasdasdassdsad', 1, '2025-03-15 19:18:04.430928', 1, '2025-03-15 19:18:19.808416', 1, 77);
INSERT INTO schema_discussion.comment OVERRIDING SYSTEM VALUE VALUES (75, 'iiiiiiiiiiiiiss', 1, '2025-03-15 19:17:31.300389', 1, '2025-03-15 19:18:35.730947', 1, NULL);


--
-- TOC entry 5246 (class 0 OID 17103)
-- Dependencies: 249
-- Data for Name: comment_vote; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--

INSERT INTO schema_discussion.comment_vote VALUES (1, 23);
INSERT INTO schema_discussion.comment_vote VALUES (1, 4);
INSERT INTO schema_discussion.comment_vote VALUES (84, 37);
INSERT INTO schema_discussion.comment_vote VALUES (1, 35);
INSERT INTO schema_discussion.comment_vote VALUES (1, 40);
INSERT INTO schema_discussion.comment_vote VALUES (1, 37);
INSERT INTO schema_discussion.comment_vote VALUES (22, 37);
INSERT INTO schema_discussion.comment_vote VALUES (22, 35);
INSERT INTO schema_discussion.comment_vote VALUES (22, 40);
INSERT INTO schema_discussion.comment_vote VALUES (1, 46);
INSERT INTO schema_discussion.comment_vote VALUES (1, 43);
INSERT INTO schema_discussion.comment_vote VALUES (1, 47);
INSERT INTO schema_discussion.comment_vote VALUES (1, 48);
INSERT INTO schema_discussion.comment_vote VALUES (1, 32);
INSERT INTO schema_discussion.comment_vote VALUES (1, 31);
INSERT INTO schema_discussion.comment_vote VALUES (1, 51);
INSERT INTO schema_discussion.comment_vote VALUES (1, 44);
INSERT INTO schema_discussion.comment_vote VALUES (1, 30);
INSERT INTO schema_discussion.comment_vote VALUES (1, 63);
INSERT INTO schema_discussion.comment_vote VALUES (1, 72);
INSERT INTO schema_discussion.comment_vote VALUES (1, 75);
INSERT INTO schema_discussion.comment_vote VALUES (1, 76);
INSERT INTO schema_discussion.comment_vote VALUES (1, 77);
INSERT INTO schema_discussion.comment_vote VALUES (1, 78);


--
-- TOC entry 5257 (class 0 OID 18648)
-- Dependencies: 260
-- Data for Name: exam; Type: TABLE DATA; Schema: schema_exam; Owner: postgres
--

INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (22, '99157936-f00c-4d5e-a235-29551209e4f4', 'Exam for Java #1', 'Trial test for java', '2025-03-07 02:54:00', '2025-03-08 01:25:00', 'END', '2025-03-06 19:48:24.017884', 76, NULL, NULL, 0);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (23, '10744e81-ae04-46e9-9836-00a83dc3598c', 'Exam for Java #1', 'Trial test for java', '2025-03-07 02:54:00', '2025-03-08 01:25:00', 'END', '2025-03-06 19:50:28.830602', 76, NULL, NULL, 0);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (19, 'code5', 'Test 3', 'Test 5', '2023-04-04 23:25:00', '2023-04-05 01:25:00', 'END', '2020-02-05 01:25:00', 76, NULL, NULL, 300);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (20, 'code6', 'Test 4', 'Test 6', '2023-04-04 23:25:00', '2023-04-05 01:25:00', 'END', '2020-02-05 01:25:00', 76, NULL, NULL, 1000);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (21, 'code7', 'Test 5', 'Test 7', '2023-04-04 23:25:00', '2023-04-05 01:25:00', 'END', '2020-02-05 01:25:00', 76, NULL, NULL, 250);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (18, 'code4', 'Test for C 2', 'Test 4', '2026-02-04 23:25:00', '2026-02-05 01:25:00', 'NOT_STARTED', '2020-02-05 01:25:00', 76, NULL, NULL, 468);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (26, '790ec861-8a7f-409a-a63d-7e5d6e2da7ef', 'Exam for Java #1', 'Trial test for java', '2026-03-06 20:05:00', '2026-03-07 18:25:00', 'NOT_STARTED', '2025-03-06 22:24:36.357302', 76, NULL, NULL, 0);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (12, 'a4768a1f-67e1-448a-84a8-a441013c4396', 'Exam for Java. Trial #1', 'Trial test for Java in FPT University', '2025-03-06 19:46:00', '2025-03-06 19:10:00', 'END', '2025-03-03 18:48:46.883106', 76, '2025-03-03 18:58:39.160939', 76, 12);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (24, 'aa14149e-15f1-47aa-a2cd-d253009d7b74', 'Exam for Java #1', 'Trial test for java', '2025-03-06 20:00:00', '2025-03-07 18:25:00', 'END', '2025-03-06 19:52:45.510527', 76, NULL, NULL, 1);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (15, 'code1', 'Test 1', 'Test 1', '2024-04-04 23:25:00', '2024-04-05 01:25:00', 'END', '2024-02-05 01:25:00', 76, NULL, NULL, 212);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (16, 'code2', 'Test 2', 'Test 2', '2024-02-02 23:25:00', '2024-02-05 01:25:00', 'END', '2023-02-05 01:25:00', 76, NULL, NULL, 160);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (17, 'code3', 'Test for C 1', 'Test 3', '2024-01-04 23:25:00', '2024-01-05 01:25:00', 'END', '2020-02-05 01:25:00', 76, NULL, NULL, 125);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (39, '6abd4451-1ada-4b20-b725-eba554fee68e', 'test test test', 'test test test', '2025-12-03 00:00:00', '2025-12-03 01:30:00', 'NOT_STARTED', '2025-03-11 11:16:23.605108', 76, '2025-03-11 11:16:41.603206', 76, 0);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (14, 'df64708b-4872-4fd7-913d-030ee5dfef78', 'Exam for Java. Trial #2', 'Trial test for java', '2025-03-06 19:45:07', '2025-03-07 11:25:00', 'END', '2025-03-03 18:56:14.865331', 76, NULL, NULL, 6);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (40, 'd3906ec6-c0c8-49c4-810f-fb7c769312d8', 'aaaaaaaaaaaaaaaaaaaaaaaa', 'aaaaaaaaaaaaaaaaaaaaaaaa', '2025-03-22 00:00:00', '2025-03-22 01:30:00', 'NOT_STARTED', '2025-03-11 15:51:26.175971', 76, '2025-03-13 07:15:50.430723', 76, 0);
INSERT INTO schema_exam.exam OVERRIDING SYSTEM VALUE VALUES (25, 'f10b5f86-ae42-40f1-b554-abc55a1e0078', 'Exam for Java. Trial #6', 'Sixth Trial test for Java in FPT University', '2025-03-16 23:43:00', '2025-03-07 18:25:00', 'NOT_STARTED', '2025-03-06 20:04:02.80897', 76, '2025-03-06 20:53:42.530983', 76, 3);


--
-- TOC entry 5261 (class 0 OID 18723)
-- Dependencies: 264
-- Data for Name: exam_language_support; Type: TABLE DATA; Schema: schema_exam; Owner: postgres
--

INSERT INTO schema_exam.exam_language_support VALUES (12, 1);
INSERT INTO schema_exam.exam_language_support VALUES (14, 1);
INSERT INTO schema_exam.exam_language_support VALUES (22, 1);
INSERT INTO schema_exam.exam_language_support VALUES (23, 1);
INSERT INTO schema_exam.exam_language_support VALUES (24, 1);
INSERT INTO schema_exam.exam_language_support VALUES (25, 1);
INSERT INTO schema_exam.exam_language_support VALUES (26, 1);
INSERT INTO schema_exam.exam_language_support VALUES (39, 1);
INSERT INTO schema_exam.exam_language_support VALUES (40, 1);


--
-- TOC entry 5258 (class 0 OID 18672)
-- Dependencies: 261
-- Data for Name: exam_participant; Type: TABLE DATA; Schema: schema_exam; Owner: postgres
--

INSERT INTO schema_exam.exam_participant VALUES (25, 91, 0);
INSERT INTO schema_exam.exam_participant VALUES (25, 93, 0);
INSERT INTO schema_exam.exam_participant VALUES (25, 1, 4.5);
INSERT INTO schema_exam.exam_participant VALUES (25, 85, 10);
INSERT INTO schema_exam.exam_participant VALUES (25, 86, 6);
INSERT INTO schema_exam.exam_participant VALUES (25, 87, 6.5);
INSERT INTO schema_exam.exam_participant VALUES (25, 88, 8);
INSERT INTO schema_exam.exam_participant VALUES (25, 89, 7);
INSERT INTO schema_exam.exam_participant VALUES (25, 90, 2);
INSERT INTO schema_exam.exam_participant VALUES (25, 92, 1);
INSERT INTO schema_exam.exam_participant VALUES (25, 94, 1.5);
INSERT INTO schema_exam.exam_participant VALUES (25, 84, 10);
INSERT INTO schema_exam.exam_participant VALUES (25, 3, 0);


--
-- TOC entry 5259 (class 0 OID 18688)
-- Dependencies: 262
-- Data for Name: exam_problem; Type: TABLE DATA; Schema: schema_exam; Owner: postgres
--

INSERT INTO schema_exam.exam_problem VALUES (14, 98, 3);
INSERT INTO schema_exam.exam_problem VALUES (14, 100, 3);
INSERT INTO schema_exam.exam_problem VALUES (14, 112, 4);
INSERT INTO schema_exam.exam_problem VALUES (12, 98, 4);
INSERT INTO schema_exam.exam_problem VALUES (12, 100, 4);
INSERT INTO schema_exam.exam_problem VALUES (12, 112, 2);
INSERT INTO schema_exam.exam_problem VALUES (22, 51, 3);
INSERT INTO schema_exam.exam_problem VALUES (22, 48, 3);
INSERT INTO schema_exam.exam_problem VALUES (22, 29, 4);
INSERT INTO schema_exam.exam_problem VALUES (23, 51, 3);
INSERT INTO schema_exam.exam_problem VALUES (23, 48, 3);
INSERT INTO schema_exam.exam_problem VALUES (23, 29, 4);
INSERT INTO schema_exam.exam_problem VALUES (24, 51, 3);
INSERT INTO schema_exam.exam_problem VALUES (24, 48, 3);
INSERT INTO schema_exam.exam_problem VALUES (24, 29, 4);
INSERT INTO schema_exam.exam_problem VALUES (39, 24, 5);
INSERT INTO schema_exam.exam_problem VALUES (39, 29, 5);
INSERT INTO schema_exam.exam_problem VALUES (25, 98, 4);
INSERT INTO schema_exam.exam_problem VALUES (25, 100, 3);
INSERT INTO schema_exam.exam_problem VALUES (25, 112, 3);
INSERT INTO schema_exam.exam_problem VALUES (26, 51, 3);
INSERT INTO schema_exam.exam_problem VALUES (26, 48, 3);
INSERT INTO schema_exam.exam_problem VALUES (26, 29, 4);
INSERT INTO schema_exam.exam_problem VALUES (40, 29, 6);
INSERT INTO schema_exam.exam_problem VALUES (40, 48, 3);
INSERT INTO schema_exam.exam_problem VALUES (40, 24, 1);


--
-- TOC entry 5260 (class 0 OID 18703)
-- Dependencies: 263
-- Data for Name: exam_submission; Type: TABLE DATA; Schema: schema_exam; Owner: postgres
--

INSERT INTO schema_exam.exam_submission VALUES (25, 1, 98, 213, 0);
INSERT INTO schema_exam.exam_submission VALUES (25, 1, 100, 214, 1.5);
INSERT INTO schema_exam.exam_submission VALUES (25, 1, 112, 215, 3);
INSERT INTO schema_exam.exam_submission VALUES (25, 84, 98, 192, 4);
INSERT INTO schema_exam.exam_submission VALUES (25, 84, 100, 193, 3);
INSERT INTO schema_exam.exam_submission VALUES (25, 84, 112, 194, 3);


--
-- TOC entry 5255 (class 0 OID 18615)
-- Dependencies: 258
-- Data for Name: language_support; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.language_support VALUES (24, 1);
INSERT INTO schema_problem.language_support VALUES (62, 1);
INSERT INTO schema_problem.language_support VALUES (97, 1);
INSERT INTO schema_problem.language_support VALUES (6, 1);
INSERT INTO schema_problem.language_support VALUES (25, 1);
INSERT INTO schema_problem.language_support VALUES (13, 1);
INSERT INTO schema_problem.language_support VALUES (27, 1);
INSERT INTO schema_problem.language_support VALUES (30, 1);
INSERT INTO schema_problem.language_support VALUES (98, 1);
INSERT INTO schema_problem.language_support VALUES (88, 1);
INSERT INTO schema_problem.language_support VALUES (29, 1);
INSERT INTO schema_problem.language_support VALUES (51, 1);
INSERT INTO schema_problem.language_support VALUES (48, 1);
INSERT INTO schema_problem.language_support VALUES (49, 1);
INSERT INTO schema_problem.language_support VALUES (50, 1);
INSERT INTO schema_problem.language_support VALUES (40, 1);
INSERT INTO schema_problem.language_support VALUES (31, 1);
INSERT INTO schema_problem.language_support VALUES (33, 1);
INSERT INTO schema_problem.language_support VALUES (34, 1);
INSERT INTO schema_problem.language_support VALUES (35, 1);
INSERT INTO schema_problem.language_support VALUES (36, 1);
INSERT INTO schema_problem.language_support VALUES (37, 1);
INSERT INTO schema_problem.language_support VALUES (38, 1);
INSERT INTO schema_problem.language_support VALUES (2, 1);
INSERT INTO schema_problem.language_support VALUES (28, 1);
INSERT INTO schema_problem.language_support VALUES (32, 1);
INSERT INTO schema_problem.language_support VALUES (42, 1);
INSERT INTO schema_problem.language_support VALUES (44, 1);
INSERT INTO schema_problem.language_support VALUES (45, 1);
INSERT INTO schema_problem.language_support VALUES (43, 1);
INSERT INTO schema_problem.language_support VALUES (39, 1);
INSERT INTO schema_problem.language_support VALUES (41, 1);
INSERT INTO schema_problem.language_support VALUES (46, 1);
INSERT INTO schema_problem.language_support VALUES (47, 1);
INSERT INTO schema_problem.language_support VALUES (100, 1);
INSERT INTO schema_problem.language_support VALUES (100, 2);
INSERT INTO schema_problem.language_support VALUES (96, 1);
INSERT INTO schema_problem.language_support VALUES (112, 1);
INSERT INTO schema_problem.language_support VALUES (112, 2);


--
-- TOC entry 5231 (class 0 OID 16730)
-- Dependencies: 234
-- Data for Name: problem; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (27, 'Sum of Two Integers', 'Given two integers a and b, return the sum of the two integers without using the operators + and -.', 'EASY', 0.00, 1, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'sum-of-two-integers');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (62, 'Valid Parentheses', 'Given an array nums of n integers, return an array of all the unique quadruplets [nums[a], nums[b], nums[c], nums[d]] such that: 0 <= a, b, c, d < n 
 a, b, c, and d are distinct.
nums[a] + nums[b] + nums[c] + nums[d] == target.
You may return the answer in any order.', 'EASY', 50.00, 20, 'PUBLIC', '2025-02-13 20:36:04.908291', 1, '2025-02-17 22:23:57.104682', 1, true, 'valid-parentheses');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (13, 'Add Two Numbers', 'You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.', 'MEDIUM', 0.00, 1, 'PUBLIC', '2025-01-02 13:29:35.752301', 1, NULL, NULL, true, 'add-two-numbers');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (96, 'Multiply Strings', 'Given two non-negative integers num1 and num2 represented as strings, return the product of num1 and num2, also represented as a string. Note: You must not use any built-in BigInteger library or convert the inputs to integer directly.', 'MEDIUM', 38.89, 18, 'PUBLIC', '2025-02-17 19:33:03.092842', 1, '2025-03-09 12:41:26.553811', 22, true, 'multiply-strings');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (24, 'Divide Two Integers', 'Given two integers dividend and divisor, divide two integers without using multiplication, division, and mod operator.

The integer division should truncate toward zero, which means losing its fractional part. For example, 8.345 would be truncated to 8, and -2.7335 would be truncated to -2.

Return the quotient after dividing dividend by divisor.

Note: Assume we are dealing with an environment that could only store integers within the 32-bit signed integer range: [−231, 231 − 1]. For this problem, if the quotient is strictly greater than 231 - 1, then return 231 - 1, and if the quotient is strictly less than -231, then return -231.', 'MEDIUM', 0.00, 1, 'PRIVATE', '2025-01-18 16:25:07.673921', 1, '2025-01-18 16:25:12.583489', 1, true, 'divide-two-integers');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (98, 'Edit Distance 1', 'Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.You have the following three operations permitted on a word: Insert a character 
 Delete a character 
 Replace a character', 'MEDIUM', 50.00, 51, 'PUBLIC', '2025-02-18 16:47:34.284482', 1, NULL, NULL, true, 'edit-distance-1');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (97, 'Edit Distance', 'Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.You have the following three operations permitted on a word: Insert a character 
 Delete a character 
 Replace a character', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-02-18 16:46:29.310675', 1, NULL, NULL, true, 'edit-distance');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (6, 'Two Sum II - Input Array Is Sorted', 'Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.

Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.

The tests are generated such that there is exactly one solution. You may not use the same element twice.

Your solution must use only constant extra space.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:56:21.718047', 1, NULL, 1, true, 'two-sum-ii--input-array-is-sorted');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (25, 'Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.', 'MEDIUM', 100.00, 2, 'PUBLIC', '2025-01-23 22:00:00', 1, '2025-01-24 10:11:02.232449', 1, true, 'two-sum');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (30, 'Distribute Elements Into Two Arrays II', 'You are given a 1-indexed array of integers nums of length n.

We define a function greaterCount such that greaterCount(arr, val) returns the number of elements in arr that are strictly greater than val.

You need to distribute all the elements of nums between two arrays arr1 and arr2 using n operations. In the first operation, append nums[1] to arr1. In the second operation, append nums[2] to arr2. Afterwards, in the ith operation:

If greaterCount(arr1, nums[i]) > greaterCount(arr2, nums[i]), append nums[i] to arr1.
If greaterCount(arr1, nums[i]) < greaterCount(arr2, nums[i]), append nums[i] to arr2.
If greaterCount(arr1, nums[i]) == greaterCount(arr2, nums[i]), append nums[i] to the array with a lesser number of elements.
If there is still a tie, append nums[i] to arr1.
The array result is formed by concatenating the arrays arr1 and arr2. For example, if arr1 == [1,2,3] and arr2 == [4,5,6], then result = [1,2,3,4,5,6].

Return the integer array result.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'distribute-elements-into-two-arrays-ii');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (29, 'Largest Merge Of Two Strings', 'ou are given two strings word1 and word2. You want to construct a string merge in the following way: while either word1 or word2 are non-empty, choose one of the following options:

If word1 is non-empty, append the first character in word1 to merge and delete it from word1.
For example, if word1 = "abc" and merge = "dv", then after choosing this operation, word1 = "bc" and merge = "dva".
If word2 is non-empty, append the first character in word2 to merge and delete it from word2.
For example, if word2 = "abc" and merge = "", then after choosing this operation, word2 = "bc" and merge = "a".
Return the lexicographically largest merge you can construct.

A string a is lexicographically larger than a string b (of the same length) if in the first position where a and b differ, a has a character strictly larger than the corresponding character in b. For example, "abcd" is lexicographically larger than "abcc" because the first position they differ is at the fourth character, and d is greater than c.', 'MEDIUM', 0.00, 0, 'PRIVATE', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'largest-merge-of-two-strings');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (88, 'First Missing Positive', 'Given an unsorted integer array nums. Return the smallest positive integer that is not present in nums. You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.', 'HARD', 0.00, 0, 'PUBLIC', '2025-02-16 23:34:15.469431', 1, '2025-02-17 16:01:35.564458', 1, true, 'first-missing-positive');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (51, 'Candy', 'There are n children standing in a line. Each child is assigned a rating value given in the integer array ratings.

You are giving candies to these children subjected to the following requirements:

Each child must have at least one candy.
Children with a higher rating get more candies than their neighbors.
Return the minimum number of candies you need to have to distribute the candies to the children.', 'HARD', 0.00, 0, 'PRIVATE', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'candy');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (48, 'Buy Two Chocolates', 'You are given an integer array prices representing the prices of various chocolates in a store. You are also given a single integer money, which represents your initial amount of money.

You must buy exactly two chocolates in such a way that you still have some non-negative leftover money. You would like to minimize the sum of the prices of the two chocolates you buy.

Return the amount of money you will have leftover after buying the two chocolates. If there is no way for you to buy two chocolates without ending up in debt, return money. Note that the leftover must be non-negative.', 'EASY', 0.00, 0, 'PRIVATE', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'buy-two-chocolates');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (100, 'Palindrome Number', 'Given an integer x, return true if x is a palindrome, and false otherwise.', 'EASY', 54.05, 37, 'PUBLIC', '2025-03-02 00:58:22.13278', 1, '2025-03-02 01:28:22.235596', 1, true, 'palindrome-number');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (49, 'Valid Number', 'Given a string s, return whether s is a valid number.

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

The digits are defined as one or more digits.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'valid-number');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (50, 'Text Justification', 'Given an array of strings words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully (left and right) justified.

You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces '' '' when necessary so that each line has exactly maxWidth characters.

Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line does not divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.

For the last line of text, it should be left-justified, and no extra space is inserted between words.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'text-justification');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (2, 'Add Two Numbers II', 'You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, 1, true, 'add-two-numbers-ii');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (40, 'Power of Two', 'Given an integer n, return true if it is a power of two. Otherwise, return false.

An integer n is a power of two, if there exists an integer x such that n == 2x.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'power-of-two');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (31, 'Find Minimum Diameter After Merging Two Trees', 'There exist two undirected trees with n and m nodes, numbered from 0 to n - 1 and from 0 to m - 1, respectively. You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the first tree and edges2[i] = [ui, vi] indicates that there is an edge between nodes ui and vi in the second tree.

You must connect one node from the first tree with another node from the second tree with an edge.

Return the minimum possible diameter of the resulting tree.

The diameter of a tree is the length of the longest path between any two nodes in the tree.', 'HARD', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'find-minimum-diameter-after-merging-two-trees');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (33, 'Split Two Strings to Make Palindrome', 'You are given two strings a and b of the same length. Choose an index and split both strings at the same index, splitting a into two strings: aprefix and asuffix where a = aprefix + asuffix, and splitting b into two strings: bprefix and bsuffix where b = bprefix + bsuffix. Check if aprefix + bsuffix or bprefix + asuffix forms a palindrome.

When you split a string s into sprefix and ssuffix, either ssuffix or sprefix is allowed to be empty. For example, if s = "abc", then "" + "abc", "a" + "bc", "ab" + "c" , and "abc" + "" are valid splits.

Return true if it is possible to form a palindrome string, otherwise return false.

Notice that x + y denotes the concatenation of strings x and y.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'split-two-strings-to-make-palindrome');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (34, 'Minimum Number of Steps to Make Two Strings Anagram II', 'You are given two strings s and t. In one step, you can append any character to either s or t.

Return the minimum number of steps to make s and t anagrams of each other.

An anagram of a string is a string that contains the same characters with a different (or the same) ordering.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'minimum-number-of-steps-to-make-two-strings-anagram-ii');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (35, 'Words Within Two Edits of Dictionary', 'You are given two string arrays, queries and dictionary. All words in each array comprise of lowercase English letters and have the same length.

In one edit you can take a word from queries, and change any letter in it to any other letter. Find all words from queries that, after a maximum of two edits, equal some word from dictionary.

Return a list of all words from queries, that match with some word from dictionary after a maximum of two edits. Return the words in the same order they appear in queries.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'words-within-two-edits-of-dictionary');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (36, 'Maximize Win From Two Segments', 'There are some prizes on the X-axis. You are given an integer array prizePositions that is sorted in non-decreasing order, where prizePositions[i] is the position of the ith prize. There could be different prizes at the same position on the line. You are also given an integer k.

You are allowed to select two segments with integer endpoints. The length of each segment must be k. You will collect all prizes whose position falls within at least one of the two selected segments (including the endpoints of the segments). The two selected segments may intersect.

For example if k = 2, you can choose segments [1, 3] and [2, 4], and you will win any prize i that satisfies 1 <= prizePositions[i] <= 3 or 2 <= prizePositions[i] <= 4.
Return the maximum number of prizes you can win if you choose the two segments optimally.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'maximize-win-from-two-segments');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (37, 'Minimum Score by Changing Two Elements', 'You are given an integer array nums.

The low score of nums is the minimum absolute difference between any two integers.
The high score of nums is the maximum absolute difference between any two integers.
The score of nums is the sum of the high and low scores.
Return the minimum score after changing two elements of nums', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'minimum-score-by-changing-two-elements');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (38, 'Longest Non-decreasing Subarray From Two Arrays', 'You are given two 0-indexed integer arrays nums1 and nums2 of length n.

Let''s define another 0-indexed integer array, nums3, of length n. For each index i in the range [0, n - 1], you can assign either nums1[i] or nums2[i] to nums3[i].

Your task is to maximize the length of the longest non-decreasing subarray in nums3 by choosing its values optimally.

Return an integer representing the length of the longest non-decreasing subarray in nums3.

Note: A subarray is a contiguous non-empty sequence of elements within an array.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'longest-nondecreasing-subarray-from-two-arrays');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (28, 'Two City Scheduling', 'A company is planning to interview 2n people. Given the array costs where costs[i] = [aCosti, bCosti], the cost of flying the ith person to city a is aCosti, and the cost of flying the ith person to city b is bCosti.

Return the minimum cost to fly every person to a city such that exactly n people arrive in each city.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'two-city-scheduling');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (32, 'Maximum XOR of Two Numbers in an Array', 'Given an integer array nums, return the maximum result of nums[i] XOR nums[j], where 0 <= i <= j < n.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'maximum-xor-of-two-numbers-in-an-array');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (112, 'Gas Station', 'There are n gas stations along a circular route, where the amount of gas at the ith station is gas[i]. You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from the ith station to its next (i + 1)th station. You begin the journey with an empty tank at one of the gas stations. Given two integer arrays gas and cost, return the starting gas station''s index if you can travel around the circuit once in the clockwise direction, otherwise return -1. If there exists a solution, it is guaranteed to be unique.', 'MEDIUM', 82.86, 35, 'PUBLIC', '2025-03-02 22:42:33.891205', 1, '2025-03-09 19:36:25.619685', 22, true, 'gas-station');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (42, 'Intersection of Two Arrays II', 'Given two integer arrays nums1 and nums2, return an array of their intersection. Each element in the result must appear as many times as it shows in both arrays and you may return the result in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'intersection-of-two-arrays-ii');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (44, 'Number of Days Between Two Dates', 'Write a program to count the number of days between two dates.

The two dates are given as strings, their format is YYYY-MM-DD as shown in the examples.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'number-of-days-between-two-dates');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (45, 'Find the Distance Value Between Two Arrays', 'Given two integer arrays arr1 and arr2, and the integer d, return the distance value between the two arrays.

The distance value is defined as the number of elements arr1[i] such that there is not any element arr2[j] where |arr1[i]-arr2[j]| <= d.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'find-the-distance-value-between-two-arrays');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (43, 'Minimum Index Sum of Two Lists', 'Given two arrays of strings list1 and list2, find the common strings with the least index sum.

A common string is a string that appeared in both list1 and list2.

A common string with the least index sum is a common string such that if it appeared at list1[i] and list2[j] then i + j should be the minimum value among all the other common strings.

Return all the common strings with the least index sum. Return the answer in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'minimum-index-sum-of-two-lists');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (39, 'Shift Distance Between Two Strings', 'You are given two strings s and t of the same length, and two integer arrays nextCost and previousCost.

In one operation, you can pick any index i of s, and perform either one of the following actions:

Shift s[i] to the next letter in the alphabet. If s[i] == ''z'', you should replace it with ''a''. This operation costs nextCost[j] where j is the index of s[i] in the alphabet.
Shift s[i] to the previous letter in the alphabet. If s[i] == ''a'', you should replace it with ''z''. This operation costs previousCost[j] where j is the index of s[i] in the alphabet.
The shift distance is the minimum total cost of operations required to transform s into t.

Return the shift distance from s to t.', 'MEDIUM', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'shift-distance-between-two-strings');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (41, 'Intersection of Two Arrays', 'Given two integer arrays nums1 and nums2, return an array of their 
intersection
. Each element in the result must be unique and you may return the result in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'intersection-of-two-arrays');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (46, 'Two Out of Three', 'Given three integer arrays nums1, nums2, and nums3, return a distinct array containing all the values that are present in at least two out of the three arrays. You may return the values in any order.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'two-out-of-three');
INSERT INTO schema_problem.problem OVERRIDING SYSTEM VALUE VALUES (47, 'Keep Multiplying Found Values by Two', 'You are given an array of integers nums. You are also given an integer original which is the first number that needs to be searched for in nums.

You then do the following steps:

If original is found in nums, multiply it by two (i.e., set original = 2 * original).
Otherwise, stop the process.
Repeat this process with the new number as long as you keep finding the number.
Return the final value of original.', 'EASY', 0.00, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, NULL, true, 'keep-multiplying-found-values-by-two');


--
-- TOC entry 5253 (class 0 OID 17620)
-- Dependencies: 256
-- Data for Name: problem_comment; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_comment VALUES (25, 1);
INSERT INTO schema_problem.problem_comment VALUES (25, 2);
INSERT INTO schema_problem.problem_comment VALUES (25, 3);
INSERT INTO schema_problem.problem_comment VALUES (25, 4);
INSERT INTO schema_problem.problem_comment VALUES (25, 5);
INSERT INTO schema_problem.problem_comment VALUES (25, 6);
INSERT INTO schema_problem.problem_comment VALUES (25, 7);
INSERT INTO schema_problem.problem_comment VALUES (96, 13);
INSERT INTO schema_problem.problem_comment VALUES (96, 14);
INSERT INTO schema_problem.problem_comment VALUES (96, 15);
INSERT INTO schema_problem.problem_comment VALUES (96, 23);
INSERT INTO schema_problem.problem_comment VALUES (25, 26);
INSERT INTO schema_problem.problem_comment VALUES (25, 27);
INSERT INTO schema_problem.problem_comment VALUES (25, 28);
INSERT INTO schema_problem.problem_comment VALUES (25, 29);
INSERT INTO schema_problem.problem_comment VALUES (96, 31);
INSERT INTO schema_problem.problem_comment VALUES (96, 32);
INSERT INTO schema_problem.problem_comment VALUES (96, 34);
INSERT INTO schema_problem.problem_comment VALUES (112, 35);
INSERT INTO schema_problem.problem_comment VALUES (112, 36);
INSERT INTO schema_problem.problem_comment VALUES (112, 37);
INSERT INTO schema_problem.problem_comment VALUES (112, 38);
INSERT INTO schema_problem.problem_comment VALUES (112, 39);
INSERT INTO schema_problem.problem_comment VALUES (112, 40);
INSERT INTO schema_problem.problem_comment VALUES (112, 41);
INSERT INTO schema_problem.problem_comment VALUES (112, 42);
INSERT INTO schema_problem.problem_comment VALUES (112, 43);
INSERT INTO schema_problem.problem_comment VALUES (112, 45);
INSERT INTO schema_problem.problem_comment VALUES (112, 46);
INSERT INTO schema_problem.problem_comment VALUES (112, 47);
INSERT INTO schema_problem.problem_comment VALUES (96, 48);
INSERT INTO schema_problem.problem_comment VALUES (96, 49);
INSERT INTO schema_problem.problem_comment VALUES (96, 50);
INSERT INTO schema_problem.problem_comment VALUES (96, 51);
INSERT INTO schema_problem.problem_comment VALUES (96, 54);
INSERT INTO schema_problem.problem_comment VALUES (96, 55);
INSERT INTO schema_problem.problem_comment VALUES (96, 56);
INSERT INTO schema_problem.problem_comment VALUES (96, 57);
INSERT INTO schema_problem.problem_comment VALUES (96, 58);
INSERT INTO schema_problem.problem_comment VALUES (96, 59);
INSERT INTO schema_problem.problem_comment VALUES (96, 60);
INSERT INTO schema_problem.problem_comment VALUES (96, 61);
INSERT INTO schema_problem.problem_comment VALUES (96, 62);
INSERT INTO schema_problem.problem_comment VALUES (96, 63);
INSERT INTO schema_problem.problem_comment VALUES (96, 64);
INSERT INTO schema_problem.problem_comment VALUES (96, 65);
INSERT INTO schema_problem.problem_comment VALUES (96, 66);
INSERT INTO schema_problem.problem_comment VALUES (96, 67);
INSERT INTO schema_problem.problem_comment VALUES (96, 68);
INSERT INTO schema_problem.problem_comment VALUES (96, 69);
INSERT INTO schema_problem.problem_comment VALUES (96, 70);
INSERT INTO schema_problem.problem_comment VALUES (96, 71);
INSERT INTO schema_problem.problem_comment VALUES (96, 72);
INSERT INTO schema_problem.problem_comment VALUES (96, 73);
INSERT INTO schema_problem.problem_comment VALUES (96, 74);
INSERT INTO schema_problem.problem_comment VALUES (96, 75);
INSERT INTO schema_problem.problem_comment VALUES (96, 76);


--
-- TOC entry 5252 (class 0 OID 17556)
-- Dependencies: 255
-- Data for Name: problem_input_parameter; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (1, 25, NULL, NULL);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (2, 25, NULL, NULL);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (133, 98, '{"name":"word1","type":"STRING"}', 1);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (134, 98, '{"name":"word2","type":"STRING"}', 1);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (137, 100, '{"name":"x","type":"INT"}', 1);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (138, 100, '{"name":"x","type":"INT"}', 2);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (29, 62, NULL, NULL);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (30, 62, NULL, NULL);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (289, 112, '{"name":"gas","type":"ARR_INT","noDimension":"1"}', 1);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (290, 112, '{"name":"cost","type":"ARR_INT","noDimension":"1"}', 1);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (291, 112, '{"name":"gas","type":"ARR_INT","noDimension":"1"}', 2);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (292, 112, '{"name":"gasSize","type":"INT"}', 2);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (293, 112, '{"name":"cost","type":"ARR_INT","noDimension":"1"}', 2);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (294, 112, '{"name":"costSize","type":"INT"}', 2);
INSERT INTO schema_problem.problem_input_parameter OVERRIDING SYSTEM VALUE VALUES (56, 88, NULL, NULL);


--
-- TOC entry 5234 (class 0 OID 16777)
-- Dependencies: 237
-- Data for Name: problem_skill; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_skill VALUES (25, 1);
INSERT INTO schema_problem.problem_skill VALUES (25, 3);
INSERT INTO schema_problem.problem_skill VALUES (25, 4);
INSERT INTO schema_problem.problem_skill VALUES (25, 5);
INSERT INTO schema_problem.problem_skill VALUES (88, 1);
INSERT INTO schema_problem.problem_skill VALUES (88, 3);
INSERT INTO schema_problem.problem_skill VALUES (62, 11);
INSERT INTO schema_problem.problem_skill VALUES (62, 1);
INSERT INTO schema_problem.problem_skill VALUES (62, 3);
INSERT INTO schema_problem.problem_skill VALUES (97, 2);
INSERT INTO schema_problem.problem_skill VALUES (97, 3);
INSERT INTO schema_problem.problem_skill VALUES (98, 2);
INSERT INTO schema_problem.problem_skill VALUES (98, 3);
INSERT INTO schema_problem.problem_skill VALUES (96, 1);
INSERT INTO schema_problem.problem_skill VALUES (96, 2);
INSERT INTO schema_problem.problem_skill VALUES (96, 3);
INSERT INTO schema_problem.problem_skill VALUES (96, 4);
INSERT INTO schema_problem.problem_skill VALUES (96, 5);
INSERT INTO schema_problem.problem_skill VALUES (96, 11);
INSERT INTO schema_problem.problem_skill VALUES (100, 1);
INSERT INTO schema_problem.problem_skill VALUES (100, 2);
INSERT INTO schema_problem.problem_skill VALUES (100, 3);
INSERT INTO schema_problem.problem_skill VALUES (100, 4);
INSERT INTO schema_problem.problem_skill VALUES (100, 5);
INSERT INTO schema_problem.problem_skill VALUES (100, 11);
INSERT INTO schema_problem.problem_skill VALUES (112, 1);
INSERT INTO schema_problem.problem_skill VALUES (112, 2);
INSERT INTO schema_problem.problem_skill VALUES (112, 3);
INSERT INTO schema_problem.problem_skill VALUES (112, 4);
INSERT INTO schema_problem.problem_skill VALUES (112, 5);
INSERT INTO schema_problem.problem_skill VALUES (112, 11);
INSERT INTO schema_problem.problem_skill VALUES (98, 1);
INSERT INTO schema_problem.problem_skill VALUES (98, 5);
INSERT INTO schema_problem.problem_skill VALUES (112, 9);
INSERT INTO schema_problem.problem_skill VALUES (112, 10);
INSERT INTO schema_problem.problem_skill VALUES (112, 13);
INSERT INTO schema_problem.problem_skill VALUES (112, 14);
INSERT INTO schema_problem.problem_skill VALUES (98, 15);
INSERT INTO schema_problem.problem_skill VALUES (98, 16);
INSERT INTO schema_problem.problem_skill VALUES (96, 9);
INSERT INTO schema_problem.problem_skill VALUES (96, 6);
INSERT INTO schema_problem.problem_skill VALUES (112, 7);
INSERT INTO schema_problem.problem_skill VALUES (112, 8);
INSERT INTO schema_problem.problem_skill VALUES (98, 7);
INSERT INTO schema_problem.problem_skill VALUES (98, 8);
INSERT INTO schema_problem.problem_skill VALUES (112, 37);


--
-- TOC entry 5236 (class 0 OID 16793)
-- Dependencies: 239
-- Data for Name: problem_solution; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (14, 62, 'Kodeholik - Editorial', 'test', true, 0, '2025-01-18 16:25:12.583489', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (82, 98, 'Kodeholik - Editorial', 'The problem requires transforming one string into another using three operations: insert, delete, or replace. This is a classic Dynamic Programming problem, where we aim to minimize the cost of edits. The solution involves comparing prefixes of both strings to compute the minimum operations required.', true, 0, '2025-01-18 16:25:12.583489', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (127, 96, 'Approach 1: Brute Force', 'Our goal is to multiply two integer numbers that are represented as strings. However, we are not allowed to use a built-in BigInteger library or convert the inputs to integers directly. So how can we multiply the two input strings? We can try to break the problem down into manageable chunks, as is done in elementary mathematics. Thus, we will focus on one digit at a time, just like in the addition example, except here we will be multiplying both numbers digit by digit.', false, 0, '2025-02-27 16:50:33.627593', 1, NULL, NULL, 6);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (39, 88, 'Kodeholik - Editorial', 'We can solve the problem by iterating through the numbers 1 to n, and use linear search to determine whether each number is in the array. The first number we cannot find is the smallest missing integer. This approach would result in a quadratic time complexity.We need to determine whether an element is in the array in constant time. Array indexing provides constant lookup time. We need to check the existence of a relatively small range of values, positive numbers between 1 and n, so we can use an array like a hash table by using the index as a key and the value as a presence indicator. The default value is false, which represents a missing number, and we set the value to true for keys that exist in nums. Numbers not in the range 1 to n are not relevant in the search for the first missing positive, so we do not mark them in the seen array. To solve the problem, we can create an array of size n + 1. For each positive number less than n in nums, we set seen[num] to true. Then, we iterate through the integers 1 to n and return the first number that is not marked as seen in the array. If the array contains all of the elements 1 to n, we return n + 1.', true, 0, '2025-01-18 16:25:12.583489', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (98, 96, 'Easiest solution', 'Remember how we do multiplication?

Start from right to left, perform multiplication on every pair of digits, and add them together', false, 0, '2025-01-18 16:25:12.583489', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (99, 96, 'Easy understand', 'If we break it into steps, it will have the following steps. 1. compute products from each pair of digits from num1 and num2. 2. carry each element over. 3. output the solution.

Things to note:

The product of two numbers cannot exceed the sum of the two lengths. (e.g. 99 * 99 cannot be five digit)', false, 0, '2025-01-18 16:25:12.583489', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (163, 112, 'Kodeholik - Editorial', 'The solution follows a greedy approach to determine the correct starting station efficiently. Step 1: Compute Net Gain at Each Station Calculate the net gas available at each station as: diff[𝑖]=gas[𝑖]−cost[𝑖]. Maintain a to variable to track the total sum of all diff[i] values. Key Insight: If sum(gas) < sum(cost), meaning to < 0, it is impossible to complete the circuit, so return -1. Step 2: Find the Valid Starting Index. Initialize index = 0 to track the potential starting station.. Traverse the array while maintaining a to variable to track the gas balance..If to (current gas balance) becomes negative at any station: --> Reset the index to i+1 (next station). --> Reset to = 0 (restart counting from this new index).', true, 0, '2025-03-09 19:36:26.554979', 22, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (128, 96, 'Just a little hhihi code', 'We dont need to do this problem. This problem is so easy.', false, 0, '2025-02-27 17:09:44.072222', 1, '2025-03-16 15:30:59.573046', 1, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (118, 96, 'Approach 1: Brute Force', 'Our goal is to multiply two integer numbers that are represented as strings. However, we are not allowed to use a built-in BigInteger library or convert the inputs to integers directly. So how can we multiply the two input strings? We can try to break the problem down into manageable chunks, as is done in elementary mathematics. Thus, we will focus on one digit at a time, just like in the addition example, except here we will be multiplying both numbers digit by digit.', false, 0, NULL, 1, NULL, 1, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (119, 96, 'Approach 1: Brute Force', 'Our goal is to multiply two integer numbers that are represented as strings. However, we are not allowed to use a built-in BigInteger library or convert the inputs to integers directly. So how can we multiply the two input strings? We can try to break the problem down into manageable chunks, as is done in elementary mathematics. Thus, we will focus on one digit at a time, just like in the addition example, except here we will be multiplying both numbers digit by digit.', false, 0, NULL, 1, NULL, 1, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (100, 96, 'Easy Java solution', 'Complexity
Time complexity:
The time complexity of the provided code is O(n * m), where n is the length of the num1 string and m is the length of the num2 string. This complexity arises from the nested loops iterating over each digit of both input strings to calculate the products.

Space complexity:
The space complexity is O(n + m), primarily due to the products array, which holds the intermediate results of the multiplication. The length of this array is equal to the sum of the lengths of the two input strings. Additionally, the StringBuilder sb also contributes to the space complexity, but its size is proportional to the number of digits in the final result, which can be at most n + m. Therefore, the dominant factor is the size of the products array.', false, 2, '2025-01-18 16:25:12.583489', 2, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (129, 96, 'Approach 1: Brute Force', 'Our goal is to multiply two integer numbers that are represented as strings. However, we are not allowed to use a built-in BigInteger library or convert the inputs to integers directly. So how can we multiply the two input strings? We can try to break the problem down into manageable chunks, as is done in elementary mathematics. Thus, we will focus on one digit at a time, just like in the addition example, except here we will be multiplying both numbers digit by digit.', false, 0, '2025-02-27 17:49:27.119516', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (101, 96, 'Multiply Strings Optimization', 'Facts You should know:

Product of two number of length n and m will be atmax of length n+m.
Eg : 9*9 =81 . Its a fact kind of thing needed for this question.

Product of digit at idx1 and idx2 will have effect only on idx1 + idx2 + 1(digit idx) and idx1 + idx2(remainder).

Boundary Cases:

If any of num is zero then Product is zero.
if one number is negative or second is Positive [here they have mentioned string consist digit only]
So no need to handle this boundary Case. But in Interview Clarify this from your Interviewer.Else you may get Rejection even after Solving Correctly.', false, 0, '2025-01-18 16:25:12.583489', 3, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (102, 96, 'AC solution in Java with explanation', '[✅ Please UPVOTE if you like this solution! ✅]', false, 0, '2025-01-18 16:25:12.583489', 4, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (103, 96, 'Best Java solution', 'Multiply according to number position.', false, 0, '2025-01-18 16:25:12.583489', 5, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (104, 96, '[Java] Easy and clean solution', 'Need to know that n1[i] * n2[j] need to be put into res[i+j] and res[i+j+1] based on observation
Need to add with previous calculated result, and consider carrier.', false, 0, '2025-01-18 16:25:12.583489', 2, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (106, 96, '10ms Java', 'We can check if(a != 0) before we enter into the nested for loop', false, 0, '2025-01-18 16:25:12.583489', 3, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (107, 96, 'Java beat 100%', '', false, 0, '2025-01-18 16:25:12.583489', 6, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (108, 96, 'One line code in Java', 'Just one line of code in java', false, 0, '2025-01-18 16:25:12.583489', 6, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (105, 96, 'Simple to understand Java code', 'Intuition
The problem appears to involve multiplying two numbers represented as strings. Using BigInteger is a common approach to handle large numbers that exceed the limits of primitive data types.

Approach
The approach here is straightforward. Convert the input strings into BigIntegers, perform the multiplication operation using the multiply() method provided by the BigInteger class, and then return the result as a string.

Complexity
Timecomplexity: O(n^2)

Spacecomplexity: O(n)', false, 0, '2025-01-18 16:25:12.583489', 6, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (109, 96, 'Simple Math Multiplication Java', 'This Java code defines a class Solution with two primary methods: add(String num1, String num2) and multiply(String num1, String num2). The class is designed to perform addition and multiplication of two large non-negative numbers represented as strings.

add(String num1, String num2):

This method takes two strings as input, which represent the numbers to be added. It initializes a StringBuilder named result to store the sum, and it uses two integer variables index and remain to keep track of the current digit index and carry.
The method iterates through the digits of both input strings, starting from the least significant digit, adds the digits along with the carry (if any), and appends the result to result.
If there any remaining carry after the iteration, it is appended to result. Finally, the result is reversed and returned as a string.
addZeros(int count):

This helper method takes an integer count as input and returns a string with count number of zeros. This is useful for aligning the partial products while performing multiplication.
multiply(String num1, String num2):

This method takes two strings as input, which represent the numbers to be multiplied.
It assigns the longer number to topNumber and the shorter number to downNumber. It initializes a list sumitionList to store the partial products of the multiplication.
The method iterates through the digits of the downNumber, multiplies each digit with the topNumber, and appends the partial products to the sumitionList.
After all partial products are computed, they are added together using the add method, and the final result is returned as a string.
In summary, this class provides a way to perform addition and multiplication operations on large non-negative numbers represented as strings.', false, 0, '2025-01-18 16:25:12.583489', 5, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (110, 96, '[Java] Simple solution', 'Multiply num2 with each element of num1 and store it in an arraylist.
Add the arraylist elements through function "addStrings". It will keep on addin each element and store the total sum in last element.
Return the last element of arraylist', false, 0, '2025-01-18 16:25:12.583489', 3, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (111, 96, 'Easiest solution', 'This solution does not rely on BigInteger or any other fancy stuff. Just plain single-digit operations. Anything higher was computed directly on Strings.
The Karatsuba algorithm is a divide an conquer algorithm that only makes 3 recursive multiplications and 4 additions. More efficient than grade school multiplication. Please note that I did a lot of string allocations which caused great overhead; this should be easily refactorable.', false, 0, '2025-01-18 16:25:12.583489', 3, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (112, 96, 'Java O(n * m)', '', false, 0, '2025-01-18 16:25:12.583489', 4, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (113, 96, 'Easy java solution', '', false, 0, '2025-01-18 16:25:12.583489', 4, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (114, 96, 'Big integer, easy approach', '', false, 0, '2025-01-18 16:25:12.583489', 2, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (115, 96, 'JAVA', '', false, 0, '2025-01-18 16:25:12.583489', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (116, 96, 'Simple and clear solution', 'I am using long multiplication technique from school program.', false, 0, '2025-01-18 16:25:12.583489', 2, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (117, 96, 'Try it if you are stuck in other solution', '', false, 0, '2025-01-18 16:25:12.583489', 2, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (133, 96, 'Just a funny code', 'Our goal is to multiply two integer numbers that are represented as strings. However, we are not allowed to use a built-in BigInteger library or convert the inputs to integers directly. So how can we multiply the two input strings? We can try to break the problem down into manageable chunks, as is done in elementary mathematics. Thus, we will focus on one digit at a time, just like in the addition example, except here we will be multiplying both numbers digit by digit.', false, 0, '2025-02-27 21:38:27.874022', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (135, 100, 'Kodeholik - Editorial', 'We can solve the problem by using remainder', true, 0, '2025-03-02 01:28:27.007599', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (172, 112, 'Best Java Solution', '# Intuition 
 
### First
To improve our runtime complexity, we need a more efficient way to check if the complement exists in the array. If the complement exists, we need to get its index. What is the best way to maintain a mapping of each element in the array to its index? A hash table.

We can reduce the lookup time from O(n) to O(1) by trading space for speed. A hash table is well suited for this purpose because it supports fast lookup in near constant time. I say "near" because if a collision occurred, a lookup could degenerate to O(n) time. However, lookup in a hash table should be amortized O(1) time as long as the hash function was chosen carefully.
 # Approach 
A simple implementation uses two iterations. In the first iteration, we add each element''s value as a key and its index as a value to the hash table. Then, in the second iteration, we check if each element''s complement (target−nums[i]) exists in the hash table. If it does exist, we return current element''s index and its complement''s index. Beware that the complement must not be nums[i] itself!
 # Complexity 
 - Time complexity: O(n).
    - We traverse the list containing n elements exactly twice. Since the hash table reduces the lookup time to O(1), the overall time complexity is O(n)
 - Space complexity:O(n).
   - The extra space required depends on the number of items stored in the hash table, which stores exactly n elements.
 <!-- Add your space complexity here, e.g. $$O(n)$$ -->
 # Code 
 <!--
Please select a code for your solution by clicking the ''Add Submission'' button. 
If you want to remove a selected code, click the ''X'' icon in the button have id that you want to removed. 
You cannot delete or edit the selected code here. 
Please choose at least one code.
-->', false, 0, '2025-03-17 00:13:55.386411', 1, NULL, NULL, 0);
INSERT INTO schema_problem.problem_solution OVERRIDING SYSTEM VALUE VALUES (173, 112, 'Best Java Solution', '# Intuition 
 
### First
To improve our runtime complexity, we need a more efficient way to check if the complement exists in the array. If the complement exists, we need to get its index. What is the best way to maintain a mapping of each element in the array to its index? A hash table.

We can reduce the lookup time from O(n) to O(1) by trading space for speed. A hash table is well suited for this purpose because it supports fast lookup in near constant time. I say "near" because if a collision occurred, a lookup could degenerate to O(n) time. However, lookup in a hash table should be amortized O(1) time as long as the hash function was chosen carefully.
 # Approach 
A simple implementation uses two iterations. In the first iteration, we add each element''s value as a key and its index as a value to the hash table. Then, in the second iteration, we check if each element''s complement (target−nums[i]) exists in the hash table. If it does exist, we return current element''s index and its complement''s index. Beware that the complement must not be nums[i] itself!
 # Complexity 
 - Time complexity: O(n).
    - We traverse the list containing n elements exactly twice. Since the hash table reduces the lookup time to O(1), the overall time complexity is O(n)
 - Space complexity:O(n).
   - The extra space required depends on the number of items stored in the hash table, which stores exactly n elements.
 <!-- Add your space complexity here, e.g. $$O(n)$$ -->
 # Code 
 <!--
Please select a code for your solution by clicking the ''Add Submission'' button. 
If you want to remove a selected code, click the ''X'' icon in the button have id that you want to removed. 
You cannot delete or edit the selected code here. 
Please choose at least one code.
-->', false, 0, '2025-03-17 00:13:55.415605', 1, NULL, NULL, 0);


--
-- TOC entry 5254 (class 0 OID 17636)
-- Dependencies: 257
-- Data for Name: problem_solution_comment; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_solution_comment VALUES (82, 8);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 9);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 10);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 11);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 16);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 17);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 19);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 20);
INSERT INTO schema_problem.problem_solution_comment VALUES (82, 21);
INSERT INTO schema_problem.problem_solution_comment VALUES (127, 30);
INSERT INTO schema_problem.problem_solution_comment VALUES (127, 44);
INSERT INTO schema_problem.problem_solution_comment VALUES (127, 52);
INSERT INTO schema_problem.problem_solution_comment VALUES (127, 53);
INSERT INTO schema_problem.problem_solution_comment VALUES (127, 77);
INSERT INTO schema_problem.problem_solution_comment VALUES (127, 78);


--
-- TOC entry 5250 (class 0 OID 17327)
-- Dependencies: 253
-- Data for Name: problem_solution_skill; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_solution_skill VALUES (82, 1);
INSERT INTO schema_problem.problem_solution_skill VALUES (82, 11);
INSERT INTO schema_problem.problem_solution_skill VALUES (100, 1);
INSERT INTO schema_problem.problem_solution_skill VALUES (100, 3);
INSERT INTO schema_problem.problem_solution_skill VALUES (100, 11);
INSERT INTO schema_problem.problem_solution_skill VALUES (39, 1);
INSERT INTO schema_problem.problem_solution_skill VALUES (39, 11);
INSERT INTO schema_problem.problem_solution_skill VALUES (14, 1);
INSERT INTO schema_problem.problem_solution_skill VALUES (14, 11);
INSERT INTO schema_problem.problem_solution_skill VALUES (163, 11);


--
-- TOC entry 5243 (class 0 OID 17004)
-- Dependencies: 246
-- Data for Name: problem_submission; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (56, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 3.15, 4, '2025-02-16 22:58:20.277696', true, NULL, NULL, 5, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (59, 1, 25, 'public static int[] twoSum(int[] nums, int target) {
 
  
int n = nums.length;for (int i = 0; i < n - 1; i++) {for (int j = i + 1; j < n; j++) {if (nums[i] + nums[j] == target) {return new int[]{i, j};}}}return new int[]{}; }', 1, NULL, 0.52, 5, '2025-02-16 23:10:41.927647', true, NULL, NULL, 5, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (61, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0.52, 5, '2025-02-17 19:33:35.860922', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (62, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0.52, 4, '2025-02-17 19:55:38.722401', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (158, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.26, 7, '2025-03-05 07:27:42.486789', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (215, 1, 112, '    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        int[] diff = new int[len];
        int to = 0;
        for (int i = 0; i < len; i++) {
            diff[i] += (gas[i] - cost[i]);
            to += diff[i];
        }
        if (to < 0) {
            return -1;
        }
        int index = 0;
        to = 0;
        for (int i = 0; i < len; i++) {
            to += diff[i];
            if (to < 0) {
                index = i + 1;
                to = 0;
            }
        }
        return index;
    }', 1, NULL, 1.97, 5, '2025-03-07 08:00:41.849753', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (63, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 3.08, 4, '2025-02-17 22:20:02.666287', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (64, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 2.88, 4, '2025-02-18 16:10:35.992705', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (65, 1, 96, 'char* multiply(char* num1, char* num2) {\nif (strcmp(num1, \"0\") == 0 || strcmp(num2, \"0\") == 0) {\nchar* zero_result = (char*)malloc(2 * sizeof(char));\nstrcpy(zero_result, \"0\");\nreturn zero_result;\n}\nint len1 = strlen(num1);\nint len2 = strlen(num2);\nint* result = (int*)calloc(len1 + len2, sizeof(int));\nfor (int i = len1 - 1; i >= 0; i--) {\nfor (int j = len2 - 1; j >= 0; j--) {\nint mul = (num1[i] - ''0'') * (num2[j] - ''0'');\nint total = mul + result[i + j + 1];\nresult[i + j] += total / 10;\nresult[i + j + 1] = total % 10;\n}\n}\nchar* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));\nint idx = 0;\nint start = 0;\nfor (int i = 0; i < len1 + len2; i++) {\nif (!(result[i] == 0 && start == 0)) {\nresult_str[idx++] = result[i] + ''0'';\nstart = 1;\n}\n}\nresult_str[idx] = ''\0'';\nfree(result);\nif (idx == 0) {\nstrcpy(result_str, \"0\");\n}\nreturn result_str;\n}', 2, NULL, 0, 0, '2025-02-18 16:13:32.323691', false, 'Compilation Error:
main.c: In function ‘multiply’:
main.c:6:41: error: stray ‘’ in program
 char* multiply(char* num1, char* num2) {nif (strcmp(num1, "0") == 0 || strcmp(num2, "0") == 0) {nchar* zero_result = (char*)malloc(2 * sizeof(char));nstrcpy(zero_result, "0");nreturn zero_result;n}nint len1 = strlen(num1);nint len2 = strlen(num2);nint* result = (int*)calloc(len1 + len2, sizeof(int));nfor (int i = len1 - 1; i >= 0; i--) {nfor (int j = len2 - 1; j >= 0; j--) {nint mul = (num1[i] - ''0'') * (num2[j] - ''0'');nint total = mul + result[i + j + 1];nresult[i + j] += total / 10;nresult[i + j + 1] = total % 10;n}n}nchar* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));nint idx = 0;nint start = 0;nfor (int i = 0; i < len1 + len2; i++) {nif (!(result[i] == 0 && start == 0)) {nresult_str[idx++] = result[i] + ''0'';nstart = 1;n}n}nresult_str[idx] = ''0'';nfree(result);nif (idx == 0) {nstrcpy(result_str, "0");n}nreturn result_str;n}
                                         ^
main.c:6:42: warning: implicit declaration of function ‘nif’ [-Wimplicit-function-declaration]
 char* multiply(char* num1, char* num2) {nif (strcmp(num1, "0") == 0 || strcmp(num2, "0") == 0) {nchar* zero_result = (char*)malloc(2 * sizeof(char));nstrcpy(zero_result, "0");nreturn zero_result;n}nint len1 = strlen(num1);nint len2 = strlen(num2);nint* result = (int*)calloc(len1 + len2, sizeof(int));nfor (int i = len1 - 1; i >= 0; i--) {nfor (int j = len2 - 1; j >= 0; j--) {nint mul = (num1[i] - ''0'') * (num2[j] - ''0'');nint total = mul + result[i + j + 1];nresult[i + j] += total / 10;nresult[i + j + 1] = total % 10;n}n}nchar* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));nint idx = 0;nint start = 0;nfor (int i = 0; i < len1 + len2; i++) {nif (!(result[i] == 0 && start == 0)) {nresult_str[idx++] = result[i] + ''0'';nstart = 1;n}n}nresult_str[idx] = ''0'';nfree(result);nif (idx == 0) {nstrcpy(result_str, "0");n}nreturn result_str;n}
                                          ^~~
main.c:6:60: error: stray ‘’ in program
 char* multiply(char* num1, char* num2) {nif (strcmp(num1, "0") == 0 || strcmp(num2, "0") == 0) {nchar* zero_result = (char*)malloc(2 * sizeof(char));nstrcpy(zero_result, "0");nreturn zero_result;n}nint len1 = strlen(num1);nint len2 = strlen(num2);nint* result = (int*)calloc(len1 + len2, sizeof(int));nfor (int i = len1 - 1; i >= 0; i--) {nfor (int j = len2 - 1; j >= 0; j--) {nint mul = (num1[i] - ''0'') * (num2[j] - ''0'');nint total = mul + result[i + j + 1];nresult[i + j] += total / 10;nresult[i + j + 1] = total % 10;n}n}nchar* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));nint idx = 0;nint start = 0;nfor (int i = 0; i < len1 + len2; i++) {nif (!(result[i] == 0 && start == 0)) {nresult_str[idx++] = result[i] + ''0'';nstart = 1;n}n}nresult_str[idx] = ''0'';nfree(result);nif (idx == 0) {nstrcpy(result_str, "0");n}nreturn result_str;n}
                                                            ^
main.c:6:61: warning: missing terminating " character
 char* multiply(char* num1, char* num2) {nif (strcmp(num1, "0") == 0 || strcmp(num2, "0") == 0) {nchar* zero_result = (char*)malloc(2 * sizeof(char));nstrcpy(zero_result, "0");nreturn zero_result;n}nint len1 = strlen(num1);nint len2 = strlen(num2);nint* result = (int*)calloc(len1 + len2, sizeof(int));nfor (int i = len1 - 1; i >= 0; i--) {nfor (int j = len2 - 1; j >= 0; j--) {nint mul = (num1[i] - ''0'') * (num2[j] - ''0'');nint total = mul + result[i + j + 1];nresult[i + j] += total / 10;nresult[i + j + 1] = total % 10;n}n}nchar* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));nint idx = 0;nint start = 0;nfor (int i = 0; i < len1 + len2; i++) {nif (!(result[i] == 0 && start == 0)) {nresult_str[idx++] = result[i] + ''0'';nstart = 1;n}n}nresult_str[idx] = ''0'';nfree(result);nif (idx == 0) {nstrcpy(result_str, "0");n}nreturn result_str;n}
                                                             ^
main.c:6:61: error: missing terminating " character
 char* multiply(char* num1, char* num2) {nif (strcmp(num1, "0") == 0 || strcmp(num2, "0") == 0) {nchar* zero_result = (char*)malloc(2 * sizeof(char));nstrcpy(zero_result, "0");nreturn zero_result;n}nint len1 = strlen(num1);nint len2 = strlen(num2);nint* result = (int*)calloc(len1 + len2, sizeof(int));nfor (int i = len1 - 1; i >= 0; i--) {nfor (int j = len2 - 1; j >= 0; j--) {nint mul = (num1[i] - ''0'') * (num2[j] - ''0'');nint total = mul + result[i + j + 1];nresult[i + j] += total / 10;nresult[i + j + 1] = total % 10;n}n}nchar* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));nint idx = 0;nint start = 0;nfor (int i = 0; i < len1 + len2; i++) {nif (!(result[i] == 0 && start == 0)) {nresult_str[idx++] = result[i] + ''0'';nstart = 1;n}n}nresult_str[idx] = ''0'';nfree(result);nif (idx == 0) {nstrcpy(result_str, "0");n}nreturn result_str;n}
                                                             ^~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
main.c:8:1: error: expected expression before ‘int’
 int main() {
 ^~~
main.c:15:1: error: expected declaration or statement at end of input
 }
 ^
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (66, 1, 96, 'char* multiply(char* num1, char* num2) {
if (strcmp(num1,"0") == 0 || strcmp(num2, "0") == 0) {
char* zero_result = (char*)malloc(2 * sizeof(char));
strcpy(zero_result, "0");
return zero_result;
}
int len1 = strlen(num1);
int len2 = strlen(num2);
int* result = (int*)calloc(len1 + len2, sizeof(int));
for (int i = len1 - 1; i >= 0; i--) {
for (int j = len2 - 1; j >= 0; j--) {
int mul = (num1[i] - ''0'') * (num2[j] - ''0'');
int total = mul + result[i + j + 1];
result[i + j] += total / 10;
result[i + j + 1] = total % 10;
}
}
char* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));
int idx = 0;
int start = 0;
for (int i = 0; i < len1 + len2; i++) {
if (!(result[i] == 0 && start == 0)) {
result_str[idx++] = result[i] + ''0'';
start = 1;
}
}
result_str[idx] = ''\0'';\nfree(result);
if (idx == 0) {
strcpy(result_str, "0");
}
return result_str;
}', 2, NULL, 0, 0, '2025-02-18 16:21:02.739256', false, 'Compilation Error:
main.c: In function ‘multiply’:
main.c:32:24: error: stray ‘’ in program
 result_str[idx] = ''0'';nfree(result);
                        ^
main.c:32:25: warning: implicit declaration of function ‘nfree’; did you mean ‘free’? [-Wimplicit-function-declaration]
 result_str[idx] = ''0'';nfree(result);
                         ^~~~~
                         free
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (67, 1, 96, 'char* multiply(char* num1, char* num2) {
if (strcmp(num1,"0") == 0 || strcmp(num2, "0") == 0) {
char* zero_result = (char*)malloc(2 * sizeof(char));
strcpy(zero_result, "0");
return zero_result;
}
int len1 = strlen(num1);
int len2 = strlen(num2);
int* result = (int*)calloc(len1 + len2, sizeof(int));
for (int i = len1 - 1; i >= 0; i--) {
for (int j = len2 - 1; j >= 0; j--) {
int mul = (num1[i] - ''0'') * (num2[j] - ''0'');
int total = mul + result[i + j + 1];
result[i + j] += total / 10;
result[i + j + 1] = total % 10;
}
}
char* result_str = (char*)malloc((len1 + len2 + 1) * sizeof(char));
int idx = 0;
int start = 0;
for (int i = 0; i < len1 + len2; i++) {
if (!(result[i] == 0 && start == 0)) {
result_str[idx++] = result[i] + ''0'';
start = 1;
}
}
result_str[idx] = ''0'';
free(result);
if (idx == 0) {
strcpy(result_str, "0");
}
return result_str;
}', 2, NULL, 3.15, 4, '2025-02-18 16:22:57.949125', false, NULL, '{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2"},{"name":"num2","type":"STRING","value":"3"}],"expectedOutput":"6","status":"Failed","actualOutput":"60"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (79, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.26, 5, '2025-02-25 13:30:57.860046', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (132, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 6, '2025-03-05 04:48:21.712767', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (133, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 6, '2025-03-05 04:48:23.254258', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (134, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 6, '2025-03-05 04:48:24.672722', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (68, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)
return i + 1;if (i == -1)
return j + 1;if (dp[i][j] != -1)
return dp[i][j];
int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-18 16:48:48.238444', false, 'Compilation Error:
Main.java:61: error: <identifier> expected
int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                          ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (69, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)
return i + 1;if (i == -1)
return j + 1;if (dp[i][j] != -1)
return dp[i][j];
int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-20 09:40:39.318066', false, 'Compilation Error:
Main.java:61: error: <identifier> expected
int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                          ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (74, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1)return i + 1;if (i == -1)return j + 1;if (dp[i][j] != -1)return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-20 09:41:47.285648', false, 'Compilation Error:
Main.java:56: error: <identifier> expected
public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1)return i + 1;if (i == -1)return j + 1;if (dp[i][j] != -1)return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (70, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)
return i + 1;if (i == -1)
return j + 1;if (dp[i][j] != -1)
return dp[i][j];
 int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-20 09:40:53.258649', false, 'Compilation Error:
Main.java:61: error: <identifier> expected
 int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                           ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (80, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.52, 5, '2025-02-25 13:33:15.674458', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (71, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)
return i + 1;if (i == -1)
return j + 1;if (dp[i][j] != -1)
return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-20 09:41:04.79656', false, 'Compilation Error:
Main.java:60: error: <identifier> expected
return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                           ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (72, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)
return i + 1;if (i == -1)
return j + 1;if (dp[i][j] != -1)return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-20 09:41:28.979633', false, 'Compilation Error:
Main.java:59: error: <identifier> expected
return j + 1;if (dp[i][j] != -1)return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                                                           ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (73, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)return i + 1;if (i == -1)return j + 1;if (dp[i][j] != -1)return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-02-20 09:41:38.515441', false, 'Compilation Error:
Main.java:57: error: <identifier> expected
return 1;}if (j == -1)return i + 1;if (i == -1)return j + 1;if (dp[i][j] != -1)return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (78, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 2.88, 4, '2025-02-25 13:27:26.749118', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (81, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.52, 5, '2025-02-25 13:35:19.491585', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (82, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.52, 6, '2025-02-25 13:36:22.50588', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (83, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.26, 6, '2025-02-25 13:37:23.832981', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (84, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 4) return result;
        
        Arrays.sort(nums); // Sort the array to apply two-pointer technique
        
        int n = nums.length;
        
        for (int i = 0; i < n - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // Skip duplicate values
            
            for (int j = i + 1; j < n - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue; // Skip duplicate values
                
                int left = j + 1, right = n - 1;
                while (left < right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        
                        while (left < right && nums[left] == nums[left + 1]) left++; // Skip duplicates
                        while (left < right && nums[right] == nums[right - 1]) right--; // Skip duplicates
                        
                        left++;
                        right--;
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return result;
    }', 1, NULL, 0.52, 7, '2025-02-25 13:42:42.956689', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (85, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 3.15, 4, '2025-02-25 19:49:22.893207', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (86, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {while(){}List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0, 0, '2025-02-25 19:49:35.439774', false, 'Compilation Error:
Main.java:57: error: illegal start of expression
public static List<List<Integer>> fourSum(int[] nums, int target) {while(){}List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}
                                                                         ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (98, 1, 62, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:08:26.227075', false, 'Compilation Error:
Main.java:9: error: cannot find symbol
         List<List<Integer>> result = fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0);
                                      ^
  symbol:   method fourSum(int[],int)
  location: class Main
Main.java:33: error: cannot find symbol
         List<List<Integer>> result = fourSum(new int[]{2, 2, 2, 2, 2}, 8);
                                      ^
  symbol:   method fourSum(int[],int)
  location: class Main
2 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (87, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return 1;}', 1, NULL, 0, 0, '2025-02-25 19:49:46.338118', false, 'Compilation Error:
Main.java:57: error: incompatible types: int cannot be converted to List<List<Integer>>
public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return 1;}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (88, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return null;}', 1, NULL, 1.05, 5, '2025-02-25 19:49:55.97581', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[1.0,0.0,-1.0,0.0,-2.0,2.0]},{"name":"target","type":"INT","value":0.0}],"expectedOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]","status":"Failed","actualOutput":"null"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (89, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0, 0, '2025-02-28 23:53:35.496667', false, 'Compilation Error:
Main.java:9: error: cannot find symbol
         BOOLEAN result = fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0);
         ^
  symbol:   class BOOLEAN
  location: class Main
Main.java:33: error: cannot find symbol
         BOOLEAN result = fourSum(new int[]{2, 2, 2, 2, 2}, 8);
         ^
  symbol:   class BOOLEAN
  location: class Main
2 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (90, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 3.15, 4, '2025-03-01 00:09:23.397744', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (91, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0, 0, '2025-03-01 21:52:13.626735', false, '"errorMessage":"2025-03-01T14:52:22.222Z bec72701-e18f-49a7-b346-0ececbb6506a Task timed out after 10.01 seconds"', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (92, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.52, 5, '2025-03-01 21:56:30.350235', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[1.0,0.0,-1.0,0.0,-2.0,2.0]},{"name":"target","type":"INT","value":0.0}],"expectedOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]","status":"Failed","actualOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (107, 1, 100, 'bool isPalindrome(int x){ 
 if(x<0 || x!=0 && x%10 ==0 ) return false; 
 int check=0; 
 while(x>check){ 
 check = check*10 + x%10; 
 x/=10; 
 } 
 return (x==check || x==check/10); 
 }', 2, NULL, 0, 0, '2025-03-02 01:02:31.669398', false, 'Compilation Error:
main.c: In function ‘main’:
main.c:19:5: error: ‘x’ undeclared (first use in this function)
     x) result;
     ^
main.c:19:5: note: each undeclared identifier is reported only once for each function it appears in
main.c:19:6: error: expected ‘;’ before ‘)’ token
     x) result;
      ^
main.c:19:6: error: expected statement before ‘)’ token
main.c:19:8: error: ‘result’ undeclared (first use in this function)
     x) result;
        ^~~~~~
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (185, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 6, '2025-03-05 08:11:00.47929', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (93, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:01:12.957243', false, 'Compilation Error:
Main.java:9: error: illegal start of expression
         static result = multiply("2", "3");
         ^
Main.java:8: error: ''try'' without ''catch'', ''finally'' or resource declarations
 try {
 ^
Main.java:9: error: <identifier> expected
         static result = multiply("2", "3");
                      ^
Main.java:10: error: <identifier> expected
            System.out.println("[Test Case 1] Output: " + gson.toJson(result));
                              ^
Main.java:10: error: illegal start of type
            System.out.println("[Test Case 1] Output: " + gson.toJson(result));
                               ^
Main.java:11: error: class, interface, enum, or record expected
 } catch (Exception e) {
   ^
Main.java:13: error: class, interface, enum, or record expected
     StackTraceElement[] stackTrace = e.getStackTrace();
     ^
Main.java:14: error: class, interface, enum, or record expected
     if (stackTrace.length > 0) {
     ^
Main.java:16: error: class, interface, enum, or record expected
         System.out.print(". Error at line: " + errorLine);
         ^
Main.java:17: error: class, interface, enum, or record expected
         try (BufferedReader br = new BufferedReader(new FileReader("Main.java"))) {
         ^
Main.java:19: error: class, interface, enum, or record expected
             String line;
             ^
Main.java:20: error: class, interface, enum, or record expected
             while ((line = br.readLine()) != null) {
             ^
Main.java:22: error: class, interface, enum, or record expected
                 if (currentLine == errorLine) {
                 ^
Main.java:24: error: class, interface, enum, or record expected
                     break;
                     ^
Main.java:25: error: class, interface, enum, or record expected
                 }
                 ^
Main.java:29: error: class, interface, enum, or record expected
         }
         ^
Main.java:34: error: class, interface, enum, or record expected
            System.out.println("[Test Case 2] Output: " + gson.toJson(result));
            ^
Main.java:35: error: class, interface, enum, or record expected
 } catch (Exception e) {
 ^
Main.java:37: error: class, interface, enum, or record expected
     StackTraceElement[] stackTrace = e.getStackTrace();
     ^
Main.java:38: error: class, interface, enum, or record expected
     if (stackTrace.length > 0) {
     ^
Main.java:40: error: class, interface, enum, or record expected
         System.out.print(". Error at line: " + errorLine);
         ^
Main.java:41: error: class, interface, enum, or record expected
         try (BufferedReader br = new BufferedReader(new FileReader("Main.java"))) {
         ^
Main.java:43: error: class, interface, enum, or record expected
             String line;
             ^
Main.java:44: error: class, interface, enum, or record expected
             while ((line = br.readLine()) != null) {
             ^
Main.java:46: error: class, interface, enum, or record expected
                 if (currentLine == errorLine) {
                 ^
Main.java:48: error: class, interface, enum, or record expected
                     break;
                     ^
Main.java:49: error: class, interface, enum, or record expected
                 }
                 ^
Main.java:53: error: class, interface, enum, or record expected
         }
         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
              ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                  ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                             ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                        ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                 ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                 ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                     ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                          ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                                                                    ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ^
43 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (94, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:02:55.058432', false, 'Compilation Error:
Main.java:9: error: illegal start of expression
         static result = multiply("2", "3");
         ^
Main.java:8: error: ''try'' without ''catch'', ''finally'' or resource declarations
 try {
 ^
Main.java:9: error: <identifier> expected
         static result = multiply("2", "3");
                      ^
Main.java:10: error: <identifier> expected
            System.out.println("[Test Case 1] Output: " + gson.toJson(result));
                              ^
Main.java:10: error: illegal start of type
            System.out.println("[Test Case 1] Output: " + gson.toJson(result));
                               ^
Main.java:11: error: class, interface, enum, or record expected
 } catch (Exception e) {
   ^
Main.java:13: error: class, interface, enum, or record expected
     StackTraceElement[] stackTrace = e.getStackTrace();
     ^
Main.java:14: error: class, interface, enum, or record expected
     if (stackTrace.length > 0) {
     ^
Main.java:16: error: class, interface, enum, or record expected
         System.out.print(". Error at line: " + errorLine);
         ^
Main.java:17: error: class, interface, enum, or record expected
         try (BufferedReader br = new BufferedReader(new FileReader("Main.java"))) {
         ^
Main.java:19: error: class, interface, enum, or record expected
             String line;
             ^
Main.java:20: error: class, interface, enum, or record expected
             while ((line = br.readLine()) != null) {
             ^
Main.java:22: error: class, interface, enum, or record expected
                 if (currentLine == errorLine) {
                 ^
Main.java:24: error: class, interface, enum, or record expected
                     break;
                     ^
Main.java:25: error: class, interface, enum, or record expected
                 }
                 ^
Main.java:29: error: class, interface, enum, or record expected
         }
         ^
Main.java:34: error: class, interface, enum, or record expected
            System.out.println("[Test Case 2] Output: " + gson.toJson(result));
            ^
Main.java:35: error: class, interface, enum, or record expected
 } catch (Exception e) {
 ^
Main.java:37: error: class, interface, enum, or record expected
     StackTraceElement[] stackTrace = e.getStackTrace();
     ^
Main.java:38: error: class, interface, enum, or record expected
     if (stackTrace.length > 0) {
     ^
Main.java:40: error: class, interface, enum, or record expected
         System.out.print(". Error at line: " + errorLine);
         ^
Main.java:41: error: class, interface, enum, or record expected
         try (BufferedReader br = new BufferedReader(new FileReader("Main.java"))) {
         ^
Main.java:43: error: class, interface, enum, or record expected
             String line;
             ^
Main.java:44: error: class, interface, enum, or record expected
             while ((line = br.readLine()) != null) {
             ^
Main.java:46: error: class, interface, enum, or record expected
                 if (currentLine == errorLine) {
                 ^
Main.java:48: error: class, interface, enum, or record expected
                     break;
                     ^
Main.java:49: error: class, interface, enum, or record expected
                 }
                 ^
Main.java:53: error: class, interface, enum, or record expected
         }
         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
              ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                  ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                             ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                        ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                 ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                         ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                 ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                     ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                          ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                                                                    ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ^
Main.java:57: error: class, interface, enum, or record expected
public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ^
43 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (95, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:04:53.455497', false, '{"isAccepted":false,"results":[{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"},{"id":2,"inputs":[{"name":"num1","type":"STRING","value":"123","isSample":false},{"name":"num2","type":"STRING","value":"456","isSample":false}],"expectedOutput":""56088"","status":"Failed","actualOutput":"56088"}],"time":"0.52","memoryUsage":6.0,"noSuccessTestcase":0,"inputWrong":{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"}}', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (96, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.52, 7, '2025-03-01 22:05:48.26489', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[1.0,0.0,-1.0,0.0,-2.0,2.0]},{"name":"target","type":"INT","value":0.0}],"expectedOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]","status":"Failed","actualOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (97, 1, 62, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:08:01.182812', false, 'Compilation Error:
Main.java:9: error: cannot find symbol
         List<List<Integer>> result = fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0);
                                      ^
  symbol:   method fourSum(int[],int)
  location: class Main
Main.java:33: error: cannot find symbol
         List<List<Integer>> result = fourSum(new int[]{2, 2, 2, 2, 2}, 8);
                                      ^
  symbol:   method fourSum(int[],int)
  location: class Main
2 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (108, 1, 100, 'bool isPalindrome(int x){ 
 if(x<0 || x!=0 && x%10 ==0 ) return false; 
 int check=0; 
 while(x>check){ 
 check = check*10 + x%10; 
 x/=10; 
 } 
 return (x==check || x==check/10); 
 }', 2, NULL, 1.57, 7, '2025-03-02 01:07:30.08449', true, NULL, NULL, 18, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (128, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 04:47:15.563849', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (99, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:08:34.94852', false, '{"isAccepted":false,"results":[{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"},{"id":2,"inputs":[{"name":"num1","type":"STRING","value":"123","isSample":false},{"name":"num2","type":"STRING","value":"456","isSample":false}],"expectedOutput":""56088"","status":"Failed","actualOutput":"56088"}],"time":"0.52","memoryUsage":7.0,"noSuccessTestcase":0,"inputWrong":{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"}}', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (100, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0.52, 7, '2025-03-01 22:09:29.732473', false, NULL, '{"id":1,"inputs":[{"name":"nums","type":"ARR_INT","value":[1.0,0.0,-1.0,0.0,-2.0,2.0]},{"name":"target","type":"INT","value":0.0}],"expectedOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]","status":"Failed","actualOutput":"[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (101, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:14:28.148606', false, '{"isAccepted":false,"results":[{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"},{"id":2,"inputs":[{"name":"num1","type":"STRING","value":"123","isSample":false},{"name":"num2","type":"STRING","value":"456","isSample":false}],"expectedOutput":""56088"","status":"Failed","actualOutput":"56088"}],"time":"0.52","memoryUsage":8.0,"noSuccessTestcase":0,"inputWrong":{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"}}', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (102, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0, 0, '2025-03-01 22:23:37.586352', false, '{"isAccepted":false,"results":[{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"},{"id":2,"inputs":[{"name":"num1","type":"STRING","value":"123","isSample":false},{"name":"num2","type":"STRING","value":"456","isSample":false}],"expectedOutput":""56088"","status":"Failed","actualOutput":"56088"}],"time":"7.86","memoryUsage":4.0,"noSuccessTestcase":0,"inputWrong":{"id":1,"inputs":[{"name":"num1","type":"STRING","value":"2","isSample":false},{"name":"num2","type":"STRING","value":"3","isSample":false}],"expectedOutput":""6"","status":"Failed","actualOutput":"6"}}', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (103, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 9.24, 4, '2025-03-01 22:36:33.021889', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (104, 1, 96, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 0, 0, '2025-03-01 22:36:40.686883', false, 'Compilation Error:
Main.java:9: error: cannot find symbol
         String result = multiply("2", "3");
                         ^
  symbol:   method multiply(String,String)
  location: class Main
Main.java:33: error: cannot find symbol
         String result = multiply("123", "456");
                         ^
  symbol:   method multiply(String,String)
  location: class Main
2 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (105, 1, 62, 'public static List<List<Integer>> fourSum(int[] nums, int target) {List<List<Integer>> result = new ArrayList<>();if (nums == null || nums.length < 4) return result; Arrays.sort(nums); int n = nums.length;for (int i = 0; i < n - 3; i++) {if (i > 0 && nums[i] == nums[i - 1]) continue; for (int j = i + 1; j < n - 2; j++) {if (j > i + 1 && nums[j] == nums[j - 1]) continue; int left = j + 1, right = n - 1; while (left < right) {long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];if (sum == target) {result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++;right--;} else if (sum < target) {left++;} else {right--;}}}}return result;}', 1, NULL, 6.09, 5, '2025-03-01 22:36:52.513696', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (106, 1, 100, 'public static boolean isPalindrome(int x) {
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.57, 6, '2025-03-02 01:01:08.770627', true, NULL, NULL, 18, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (109, 1, 100, 'public static boolean isPalindrome(int x) {
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 2, NULL, 0, 0, '2025-03-02 01:07:33.431597', false, 'Compilation Error:
main.c:8:8: error: expected ‘=’, ‘,’, ‘;’, ‘asm’ or ‘__attribute__’ before ‘static’
 public static boolean isPalindrome(int x) {
        ^~~~~~
main.c:8:15: error: unknown type name ‘boolean’
 public static boolean isPalindrome(int x) {
               ^~~~~~~
main.c: In function ‘main’:
main.c:23:14: warning: implicit declaration of function ‘isPalindrome’; did you mean ‘isalnum’? [-Wimplicit-function-declaration]
     result = isPalindrome(121);
              ^~~~~~~~~~~~
              isalnum
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (110, 1, 100, 'public static boolean isPalindrome(int x) {
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 7, '2025-03-02 01:07:40.122353', true, NULL, NULL, 18, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (129, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.26, 6, '2025-03-05 04:47:43.747255', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (130, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 6, '2025-03-05 04:47:45.590354', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (131, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 6, '2025-03-05 04:47:47.150832', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (135, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 9.18, 4, '2025-03-05 06:55:01.077153', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (136, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 8.39, 4, '2025-03-05 06:55:02.86536', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (137, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 06:55:04.203219', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (138, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 5, '2025-03-05 06:56:20.931373', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (139, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 5, '2025-03-05 06:56:22.495073', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (140, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 06:56:24.003719', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (141, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 9.04, 4, '2025-03-05 07:07:26.190859', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (142, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.31, 4, '2025-03-05 07:07:28.2094', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (143, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 07:07:30.012918', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (119, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1) return i + 1;if (i == -1) return j + 1;if (dp[i][j] != -1) return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-03-05 04:31:59.643034', false, 'Compilation Error:
Main.java:57: error: <identifier> expected
public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1) return i + 1;if (i == -1) return j + 1;if (dp[i][j] != -1) return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (120, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) { 
 if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1) return i + 1;if (i == -1) return j + 1;if (dp[i][j] != -1) return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-03-05 04:33:30.733798', false, 'Compilation Error:
Main.java:58: error: <identifier> expected
 if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1) return i + 1;if (i == -1) return j + 1;if (dp[i][j] != -1) return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (121, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) { 
 if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1) return i + 1;if (i == -1) return j + 1;if (dp[i][j] != -1) return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publi cstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', 1, NULL, 0, 0, '2025-03-05 04:34:49.308815', false, 'Compilation Error:
Main.java:58: error: '';'' expected
 if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else return 1;}if (j == -1) return i + 1;if (i == -1) return j + 1;if (dp[i][j] != -1) return dp[i][j]; int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publi cstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (122, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 11.01, 5, '2025-03-05 04:36:39.536478', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (123, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 9.7, 4, '2025-03-05 04:44:30.093121', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (124, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.31, 4, '2025-03-05 04:44:31.702502', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (125, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 04:44:33.075846', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (126, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.79, 5, '2025-03-05 04:47:12.426648', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (127, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 5, '2025-03-05 04:47:14.05298', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (144, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 13.63, 4, '2025-03-05 07:14:28.269085', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (145, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.57, 4, '2025-03-05 07:14:29.891619', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (146, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 07:14:31.346244', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (147, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 5, '2025-03-05 07:16:28.266377', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (148, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 5, '2025-03-05 07:16:29.87806', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (149, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 07:16:31.237722', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (150, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.26, 6, '2025-03-05 07:19:04.408619', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (151, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 0.79, 6, '2025-03-05 07:19:06.022418', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (152, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 6, '2025-03-05 07:19:07.519983', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (153, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 6, '2025-03-05 07:24:01.039391', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (154, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 0.79, 7, '2025-03-05 07:24:02.638687', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (155, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.26, 7, '2025-03-05 07:24:04.009995', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (156, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 7, '2025-03-05 07:27:39.51507', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (157, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 0.79, 7, '2025-03-05 07:27:41.184218', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (159, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 8, '2025-03-05 07:31:22.642337', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (160, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 0.52, 8, '2025-03-05 07:31:24.2038', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (161, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 8, '2025-03-05 07:31:25.529821', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (162, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 8, '2025-03-05 07:35:01.6358', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (163, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 1.05, 9, '2025-03-05 07:35:03.376932', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (164, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0, 9, '2025-03-05 07:35:04.731621', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (165, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 9, '2025-03-05 07:37:11.971373', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (166, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 1.05, 9, '2025-03-05 07:37:13.462092', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (167, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0, 9, '2025-03-05 07:37:14.837476', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (168, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return 0; 
 }', 1, NULL, 0, 10, '2025-03-05 07:38:01.23891', false, NULL, '{"id":1,"inputs":[{"name":"word1","type":"STRING","value":"horse"},{"name":"word2","type":"STRING","value":"ros"}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (169, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 0.52, 10, '2025-03-05 07:38:02.821232', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (170, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return 0; 
 }', 1, NULL, 0.52, 10, '2025-03-05 07:38:04.190791', false, NULL, '{"id":1,"inputs":[{"name":"gas","type":"ARR_INT","value":[1.0,2.0,3.0,4.0,5.0]},{"name":"cost","type":"ARR_INT","value":[3.0,4.0,5.0,1.0,2.0]}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 1, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (200, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 2.62, 5, '2025-03-06 13:31:17.711129', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (171, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return 0; 
 }', 1, NULL, 10.09, 4, '2025-03-05 08:01:33.244926', false, NULL, '{"id":1,"inputs":[{"name":"word1","type":"STRING","value":"horse"},{"name":"word2","type":"STRING","value":"ros"}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (172, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 1.7, 4, '2025-03-05 08:01:34.933102', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (173, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return 0; 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:01:36.389686', false, NULL, '{"id":1,"inputs":[{"name":"gas","type":"ARR_INT","value":[1.0,2.0,3.0,4.0,5.0]},{"name":"cost","type":"ARR_INT","value":[3.0,4.0,5.0,1.0,2.0]}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 1, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (174, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:02:51.272733', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (175, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.31, 5, '2025-03-05 08:02:52.824536', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (176, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.79, 5, '2025-03-05 08:02:54.245113', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (177, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 10.75, 4, '2025-03-05 08:10:23.738963', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (178, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.57, 4, '2025-03-05 08:10:25.527572', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (179, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:10:27.003194', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (180, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:10:45.169945', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (181, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 5, '2025-03-05 08:10:46.920344', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (182, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:10:48.455735', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (183, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:10:57.536439', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (184, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 6, '2025-03-05 08:10:59.077708', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (186, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 11.01, 4, '2025-03-05 08:35:32.60393', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (187, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.57, 4, '2025-03-05 08:35:34.286614', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (188, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:35:35.785607', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (189, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 0.79, 5, '2025-03-05 08:37:10.485483', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (190, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.05, 5, '2025-03-05 08:37:12.122288', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (191, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 08:37:13.59854', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (192, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 return solve(m - 1, n - 1, word1, word2, dp); 
 }', 1, NULL, 10.62, 4, '2025-03-05 16:19:11.401207', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (193, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', 1, NULL, 1.57, 4, '2025-03-05 16:19:13.189383', true, NULL, NULL, 9, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (194, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 0.52, 5, '2025-03-05 16:19:14.617281', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (195, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 0; 
 }', 1, NULL, 0, 0, '2025-03-05 16:44:08.034272', false, 'Compilation Error:
Main.java:74: error: not a statement
 0; 
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (196, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 10.75, 4, '2025-03-05 16:44:10.407925', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (197, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 1.84, 5, '2025-03-05 16:44:12.282137', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (198, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 0; 
 }', 1, NULL, 0, 0, '2025-03-06 13:31:14.283338', false, 'Compilation Error:
Main.java:74: error: not a statement
 0; 
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (199, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 10.75, 4, '2025-03-06 13:31:16.082407', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (201, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 11.8, 4, '2025-03-06 16:52:30.005001', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (202, 1, 96, 'b', 2, NULL, 0, 0, '2025-03-06 16:52:37.702086', false, 'Compilation Error:
main.c:10:1: error: expected ‘=’, ‘,’, ‘;’, ‘asm’ or ‘__attribute__’ before ‘int’
 int main() {
 ^~~
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (203, 1, 96, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', 1, NULL, 0.66, 4, '2025-03-06 16:52:49.102035', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (204, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 0; 
 }', 1, NULL, 0, 0, '2025-03-06 18:03:58.957323', false, 'Compilation Error:
Main.java:74: error: not a statement
 0; 
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (205, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 9.18, 4, '2025-03-06 18:04:01.08706', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (206, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 1.84, 5, '2025-03-06 18:04:02.450007', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (207, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 0; 
 }', 1, NULL, 0, 0, '2025-03-06 19:46:35.834493', false, 'Compilation Error:
Main.java:74: error: not a statement
 0; 
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (208, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 11.01, 4, '2025-03-06 19:46:37.534171', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (209, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 2.36, 5, '2025-03-06 19:46:39.021587', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (210, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 int m = word1.length(), n = word2.length(); 
 int[][] dp = new int[m + 1][n + 1]; 
 for (int[] row : dp) Arrays.fill(row, -1); 
 0; 
 }', 1, NULL, 0, 0, '2025-03-06 20:28:13.007054', false, 'Compilation Error:
Main.java:74: error: not a statement
 0; 
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (211, 1, 100, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return true; 
 }', 1, NULL, 12.58, 4, '2025-03-06 20:28:14.659864', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (212, 1, 112, 'public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', 1, NULL, 2.1, 5, '2025-03-06 20:28:16.031217', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (214, 1, 100, '    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        int reverse = 0;
        int xcopy = x;
        while (x > 0) {
            reverse = (reverse * 10) + (x % 10);
            x /= 10;
        }
        return true;
    }', 1, NULL, 10.35, 4, '2025-03-07 08:00:40.426865', false, NULL, '{"id":3,"inputs":[{"name":"x","type":"INT","value":10.0}],"expectedOutput":"false","status":"Failed","actualOutput":"true"}', 6, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (217, 1, 98, 's', 1, NULL, 0, 0, '2025-03-10 17:29:26.532739', false, 'Compilation Error:
Main.java:57: error: <identifier> expected
s
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (218, 1, 98, 'return 0', 1, NULL, 0, 0, '2025-03-10 17:29:49.331011', false, 'Compilation Error:
Main.java:57: error: illegal start of type
return 0
^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (219, 1, 98, 'return 0;', 1, NULL, 0, 0, '2025-03-10 17:29:56.606159', false, 'Compilation Error:
Main.java:57: error: illegal start of type
return 0;
^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (220, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 return 0;}', 1, NULL, 0, 0, '2025-03-10 17:30:17.319103', false, 'Compilation Error:
Main.java:9: error: cannot find symbol
         int result = minDistance("horse", "ros");
                      ^
  symbol:   method minDistance(String,String)
  location: class Main
Main.java:33: error: cannot find symbol
         int result = minDistance("intention", "execution");
                      ^
  symbol:   method minDistance(String,String)
  location: class Main
2 errors
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (221, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 return 0; 
}', 1, NULL, 0.52, 5, '2025-03-10 17:30:39.934168', false, NULL, '{"id":1,"inputs":[{"name":"word1","type":"STRING","value":"horse"},{"name":"word2","type":"STRING","value":"ros"}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (222, 1, 98, 'public static int solve(int i, int j, String s1, String s2, int[][] dp) { 
 if (i == -1) return j + 1; 
 if (j == -1) return i + 1; 
 if (dp[i][j] != -1) return dp[i][j]; 
 if (s1.charAt(i) == s2.charAt(j)) { 
 return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp); 
 } else { 
 int insert = 1 + solve(i, j - 1, s1, s2, dp); 
 int replace = 1 + solve(i - 1, j - 1, s1, s2, dp); 
 int delete = 1 + solve(i - 1, j, s1, s2, dp); 
 return dp[i][j] = Math.min(insert, Math.min(replace, delete)); 
 } 
 } 
 public static int minDistance(String word1, String word2) { 
 return 0; 
}', 1, NULL, 0.52, 5, '2025-03-10 20:41:44.504363', false, NULL, '{"id":1,"inputs":[{"name":"word1","type":"STRING","value":"horse"},{"name":"word2","type":"STRING","value":"ros"}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (223, 1, 100, 'bool isPalindrome(int x){ 
 if(x<0 || x!=0 && x%10 ==0 ) return false; 
 int check=0; 
 while(x>check){ 
 check = check*10 + x%10; 
 x/=10; 
 } 
 return (x==check || x==check/10); 
 }', 2, NULL, 12.32, 4, '2025-03-10 22:04:34.870248', false, NULL, '{"id":1,"inputs":[{"name":"x","type":"INT","value":121.0}],"expectedOutput":"true","status":"Failed","actualOutput":"Unsupported return type"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (224, 1, 100, 'bool isPalindrome(int x){ 
 if(x<0 || x!=0 && x%10 ==0 ) return false; 
 int check=0; 
 while(x>check){ 
 check = check*10 + x%10; 
 x/=10; 
 } 
 return (x==check || x==check/10); 
 }', 2, NULL, 1.31, 4, '2025-03-10 22:04:49.660747', false, NULL, '{"id":1,"inputs":[{"name":"x","type":"INT","value":121.0}],"expectedOutput":"true","status":"Failed","actualOutput":"Unsupported return type"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (225, 1, 112, 'int canCompleteCircuit(int* gas, int gasSize, int* cost, int costSize){ 
 int gas_tank = 0, start_index = 0, sum = 0; 
 for (int i=0; i<costSize; i++) { 
 sum += gas[i] - cost[i]; 
 gas_tank += gas[i] - cost[i]; 
 if (gas_tank < 0', 2, NULL, 0, 0, '2025-03-10 22:05:56.387701', false, 'Compilation Error:
main.c: In function ‘canCompleteCircuit’:
main.c:15:1: error: expected ‘)’ before ‘int’
 int main() {
 ^~~
main.c:22:1: error: expected declaration or statement at end of input
 }
 ^
main.c:22:1: error: expected declaration or statement at end of input
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (226, 1, 112, 'int canCompleteCircuit(int* gas, int gasSize, int* cost, int costSize){ 
 int gas_tank = 0, start_index = 0, sum = 0; 
 for (int i=0; i<costSize; i++) { 
 sum += gas[i] - cost[i]; 
 gas_tank += gas[i] - cost[i]; 
 if (gas_tank < 0) { 
 start_index = i+1; 
 gas_tank = 0; 
 } 
 } 
 return sum < 0 ? -1 : start_index; 
 }', 2, NULL, 2.1, 5, '2025-03-10 22:06:21.400804', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (227, 1, 112, 'int canCompleteCircuit(int* gas, int gasSize, int* cost, int costSize){ 
 int gas_tank = 0, start_index = 0, sum = 0; 
 for (int i=0; i<costSize; i++) { 
 sum += gas[i] - cost[i]; 
 gas_tank += gas[i] - cost[i]; 
 if (gas_tank < 0) { 
 start_index = i+1; 
 gas_tank = 0; 
 } 
 } 
 return 0; 
 }', 2, NULL, 10.75, 4, '2025-03-10 22:08:43.755429', false, NULL, '{"id":1,"inputs":[{"name":"gas","type":"ARR_INT","value":[1.0,2.0,3.0,4.0,5.0]},{"name":"gasSize","type":"INT","value":5.0},{"name":"cost","type":"ARR_INT","value":[3.0,4.0,5.0,1.0,2.0]},{"name":"costSize","type":"INT","value":5.0}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (216, 1, 98, '    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        int[] diff = new int[len];
        int to = 0;
        for (int i = 0; i < len; i++) {
            diff[i] += (gas[i] - cost[i]);
            to += diff[i];
        }
        if (to < 0) {
            return -1;
        }
        int index = 0;
        to = 0;
        for (int i = 0; i < len; i++) {
            to += diff[i];
            if (to < 0) {
                index = i + 1;
                to = 0;
            }
        }
        return index;
    }', 1, NULL, 8.39, 4, '2025-03-10 17:28:44.836089', true, NULL, NULL, 2, 'SUCCESS');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (213, 1, 98, '    public static int solve(int i, int j, String s1, String s2, int[][] dp) {
        if (i == -1)
            return j + 1;
        if (j == -1)
            return i + 1;
        if (dp[i][j] != -1)
            return dp[i][j];
        if (s1.charAt(i) == s2.charAt(j)) {
            return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);
        } else {
            int insert = 1 + solve(i, j - 1, s1, s2, dp);
            int replace = 1 + solve(i - 1, j - 1, s1, s2, dp);
            int delete = 1 + solve(i - 1, j, s1, s2, dp);
            return dp[i][j] = Math.min(insert, Math.min(replace, delete));
        }
    }

    public static int minDistance(String word1, String word2) { 
        int m = word1.length(), n = word2.length(); 
        int[][] dp = new int[m + 1][n + 1]; 
        for (int[] row : dp) Arrays.fill(row, -1); 
        return 0; 
    }', 1, NULL, 0, 0, '2025-03-07 08:00:38.738131', false, 'Compilation Error:
Main.java:74: error: not a statement
 0; 
 ^
1 error
', NULL, 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (228, 1, 24, '', 1, NULL, 0.1, 5, '2025-03-10 20:36:04.908291', false, NULL, NULL, 5, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (229, 1, 13, '', 1, NULL, 0.1, 5, '2025-03-12 20:36:04.908291', false, NULL, NULL, 5, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (230, 1, 27, '', 1, NULL, 0.1, 5, '2025-03-12 20:36:04.908291', false, NULL, NULL, 5, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (231, 1, 112, '//Test 
//Function 
 public static int canCompleteCircuit(int[] gas, int[] cost) {
    return 0;
}', 1, NULL, 10.75, 4, '2025-03-15 17:02:55.674634', false, NULL, '{"id":1,"inputs":[{"name":"gas","type":"ARR_INT","value":[1.0,2.0,3.0,4.0,5.0]},{"name":"cost","type":"ARR_INT","value":[3.0,4.0,5.0,1.0,2.0]}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');
INSERT INTO schema_problem.problem_submission OVERRIDING SYSTEM VALUE VALUES (232, 1, 112, '//Test 
//Function 
 public static int canCompleteCircuit(int[] gas, int[] cost) {
    return 0;
}', 1, NULL, 11.53, 4, '2025-03-15 18:36:13.021299', false, NULL, '{"id":1,"inputs":[{"name":"gas","type":"ARR_INT","value":[1.0,2.0,3.0,4.0,5.0]},{"name":"cost","type":"ARR_INT","value":[3.0,4.0,5.0,1.0,2.0]}],"expectedOutput":"3","status":"Failed","actualOutput":"0"}', 0, 'FAILED');


--
-- TOC entry 5239 (class 0 OID 16868)
-- Dependencies: 242
-- Data for Name: problem_template; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (160, 112, 1, '//Test 
//Function 
 public static int canCompleteCircuit(int[] gas, int[] cost) {
}', 'canCompleteCircuit', 'INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (161, 112, 2, '//Test 
//Function 
 int canCompleteCircuit(int* gas, int gasSize, int* cost, int costSize) {
}', 'canCompleteCircuit', 'INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (134, 96, 1, '//Function
public static String multiply(String num1, String num2) {
}', 'multiply', 'STRING');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (18, 62, 1, '//Function 
public static List<List<Integer>> fourSum(int[] nums, int target) {
}', 'fourSum', 'ARR_INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (44, 88, 1, '//Function
public int firstMissingPositive(int[] nums) {
}', 'firstMissingPositive', 'INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (90, 98, 1, '//Function
public static int minDistance(String word1, String word2) {
}', 'minDistance', 'INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (2, 25, 1, '//Function
public int[] twoSum(int[] nums, int target) {}', 'twoSum', 'ARR_INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (3, 25, 2, '//Function
int* twoSum(int* nums, int numsSize, int target, int* returnSize) {}', 'twoSum', 'ARR_INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (91, 98, 2, '//Function
public int minDistance(char* word1, char* word2) {
}', 'minDistance', 'INT');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (93, 100, 2, '//Function
bool isPalindrome(int x) {
}', 'isPalindrome', 'BOOLEAN');
INSERT INTO schema_problem.problem_template OVERRIDING SYSTEM VALUE VALUES (92, 100, 1, '//Function
public static boolean isPalindrome(int x) {
}', 'isPalindrome', 'BOOLEAN');


--
-- TOC entry 5233 (class 0 OID 16762)
-- Dependencies: 236
-- Data for Name: problem_topic; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem_topic VALUES (25, 2);
INSERT INTO schema_problem.problem_topic VALUES (25, 3);
INSERT INTO schema_problem.problem_topic VALUES (25, 9);
INSERT INTO schema_problem.problem_topic VALUES (112, 19);
INSERT INTO schema_problem.problem_topic VALUES (112, 9);
INSERT INTO schema_problem.problem_topic VALUES (112, 2);
INSERT INTO schema_problem.problem_topic VALUES (96, 1);
INSERT INTO schema_problem.problem_topic VALUES (96, 3);
INSERT INTO schema_problem.problem_topic VALUES (96, 4);
INSERT INTO schema_problem.problem_topic VALUES (96, 5);
INSERT INTO schema_problem.problem_topic VALUES (98, 1);
INSERT INTO schema_problem.problem_topic VALUES (98, 2);
INSERT INTO schema_problem.problem_topic VALUES (88, 9);
INSERT INTO schema_problem.problem_topic VALUES (88, 2);
INSERT INTO schema_problem.problem_topic VALUES (98, 3);
INSERT INTO schema_problem.problem_topic VALUES (98, 4);
INSERT INTO schema_problem.problem_topic VALUES (98, 5);
INSERT INTO schema_problem.problem_topic VALUES (100, 1);
INSERT INTO schema_problem.problem_topic VALUES (100, 2);
INSERT INTO schema_problem.problem_topic VALUES (100, 3);
INSERT INTO schema_problem.problem_topic VALUES (100, 4);
INSERT INTO schema_problem.problem_topic VALUES (100, 5);
INSERT INTO schema_problem.problem_topic VALUES (62, 2);
INSERT INTO schema_problem.problem_topic VALUES (62, 1);
INSERT INTO schema_problem.problem_topic VALUES (62, 9);
INSERT INTO schema_problem.problem_topic VALUES (112, 1);
INSERT INTO schema_problem.problem_topic VALUES (112, 3);
INSERT INTO schema_problem.problem_topic VALUES (112, 4);
INSERT INTO schema_problem.problem_topic VALUES (112, 5);
INSERT INTO schema_problem.problem_topic VALUES (96, 9);
INSERT INTO schema_problem.problem_topic VALUES (96, 2);
INSERT INTO schema_problem.problem_topic VALUES (96, 6);
INSERT INTO schema_problem.problem_topic VALUES (97, 6);
INSERT INTO schema_problem.problem_topic VALUES (97, 9);
INSERT INTO schema_problem.problem_topic VALUES (98, 6);
INSERT INTO schema_problem.problem_topic VALUES (98, 9);
INSERT INTO schema_problem.problem_topic VALUES (100, 9);


--
-- TOC entry 5237 (class 0 OID 16840)
-- Dependencies: 240
-- Data for Name: solution_code; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.solution_code VALUES (14, 62, 1, 'public static List<List<Integer>> fourSum(int[] nums, int target) {
Arrays.sort(nums);
return kSum(nums, target, 0, 4);
}
public static List<List<Integer>> kSum(int[] nums, long target, int start, int k) {
List<List<Integer>> res = new ArrayList<>();
if (start == nums.length) {
return res;
}
long average_value = target / k;
if (
nums[start] > average_value || average_value > nums[nums.length - 1]
) {
return res;
}
if (k == 2) {
return twoSum(nums, target, start);
}
for (int i = start; i < nums.length; ++i) {
if (i == start || nums[i - 1] != nums[i]) {
for (List<Integer> subset : kSum(
nums,
target - nums[i],
i + 1,
k - 1
)) {
res.add(new ArrayList<>(Arrays.asList(nums[i])));
res.get(res.size() - 1).addAll(subset);
}
}
}
return res;
}
public static List<List<Integer>> twoSum(int[] nums, long target, int start) {
List<List<Integer>> res = new ArrayList<>();
int lo = start, hi = nums.length - 1;
while (lo < hi) {
int currSum = nums[lo] + nums[hi];
if (currSum < target || (lo > start && nums[lo] == nums[lo - 1])) {
++lo;
} else if (
currSum > target ||
(hi < nums.length - 1 && nums[hi] == nums[hi + 1])
) {
--hi;
} else {
res.add(Arrays.asList(nums[lo++], nums[hi--]));
}
}
return res;
}', NULL);
INSERT INTO schema_problem.solution_code VALUES (82, 98, 1, 'public static int solve(int i, int j, String s1, String s2, int dp[][]) {if (i == 0 && j == 0) {if (s1.charAt(i) == s2.charAt(j))return 0;else
return 1;}if (j == -1)
return i + 1;if (i == -1)
return j + 1;if (dp[i][j] != -1)
return dp[i][j];
int insert = 300000000;int replace = 30000000;int delete = 30000000;if (s1.charAt(i) == s2.charAt(j))return dp[i][j] = solve(i - 1, j - 1, s1, s2, dp);else {insert = 1 + solve(i, j - 1, s1, s2, dp); replace = 1 + solve(i - 1, j - 1, s1, s2, dp);delete = 1 + solve(i - 1, j, s1, s2, dp);}return dp[i][j] = Math.min(insert, Math.min(replace, delete));}publicstatic int minDistance(String word1, String word2) {int dp[][] = new int[word1.length()][word2.length()];for (int[] I : dp)Arrays.fill(I, -1);return solve(word1.length() - 1, word2.length() - 1, word1, word2, dp);}', NULL);
INSERT INTO schema_problem.solution_code VALUES (100, 96, 1, 'public static String multiply(String num1, String num2) { \n int[] num = new int[num1.length()+num2.length()]; \n int len1 = num1.length(), len2 = num2.length(); \n for(int i=len1-1;i>=0;i--){ \n for(int j=len2-1;j>=0;j--){ \n int temp = (num1.charAt(i)-''0'')*(num2.charAt(j)-''0''); \n num[i+j] += (temp+num[i+j+1])/10; \n num[i+j+1] = (num[i+j+1]+temp)%10; \n } \n } \n StringBuilder sb = new StringBuilder(); \n for(int i: num) if(sb.length()>0||i>0)  sb.append(i); \n return (sb.length()==0)?\"0\":sb.toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (101, 96, 1, 'public static String multiply(String a, String b) { \n if (a.equals(\"0\") || b.equals(\"0\")) { \n return \"0\"; \n } \n int m = a.length() - 1, n = b.length() - 1, carry = 0; \n String product = \"\"; \n for (int i = 0; i <= m + n || carry != 0; ++i) { \n for (int j = Math.max(0, i - n); j <= Math.min(i, m); ++j) { \n carry += (a.charAt(m - j) - ''0'') * (b.charAt(n - i + j) - ''0''); \n } \n product += (char)(carry % 10 + ''0''); \n carry /= 10; \n } \n return new StringBuilder(product).reverse().toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (102, 96, 1, 'public static String multiply(String num1, String num2) { \n int len1 = num1.length(), len2 = num2.length(); \n int[] prod = new int [len1 + len2]; \n int currIdx = prod.length-1; \n for(int i = len1-1; i >= 0; i--) { \n int idx = currIdx--; \n for(int j = len2-1; j >= 0; j--) { \n int a = num1.charAt(i) - ''0''; \n int b = num2.charAt(j) - ''0''; \n int res = a * b + prod[idx]; \n prod[idx] = res % 10; \n prod[--idx] += res / 10; \n } \n } \n StringBuilder sb = new StringBuilder(); \n for(int num : prod) { \n if(num == 0 && sb.length() == 0) continue; \n sb.append(num); \n } \n if(sb.length() == 0) return "0"; \n return sb.toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (103, 96, 1, 'public static String multiply(String num1, String num2) {\n BigInteger n1 = new BigInteger(num1);\n BigInteger n2 = new BigInteger(num2);\n BigInteger n3 = n1.multiply(n2);\n return n3.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (104, 96, 1, 'public static String multiply(String num1, String num2) { \n BigInteger a = new BigInteger(num1); \n BigInteger b = new BigInteger(num2); \n BigInteger c = a.multiply(b); \n return String.valueOf(c); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (105, 96, 1, 'public static String multiply(String num1, String num2) { \n return String.valueOf((new java.math.BigInteger(num1)).multiply(new java.math.BigInteger(num2))); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (106, 96, 1, 'public static String multiply(String nums1, String nums2) { \n if(nums1.equals("0") || nums2.equals("0")) return "0"; \n if(nums1.equals("1")) return nums2; \n if(nums2.equals("1")) return nums1; \n int arr[]=new int[nums1.length()+nums2.length()]; \n for(int i=nums1.length()-1;i>=0;i--){ \n for(int j=nums2.length()-1;j>=0;j--){ \n int prod=(nums1.charAt(i)-''0'')*(nums2.charAt(j)-''0''); \n prod+=arr[i+j+1]; \n arr[i+j+1]=prod%10; \n arr[i+j]+=prod/10; \n } \n } \n StringBuilder ans=new StringBuilder(); \n for(int i=0;i<arr.length;i++){ \n if(ans.length()==0 && arr[i]==0) continue; \n ans.append(arr[i]); \n } \n return ans.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (107, 96, 1, 'public static String multiply(String num1, String num2) { \n BigInteger a = new BigInteger(num1); \n BigInteger b = new BigInteger(num2); \n BigInteger c = a.multiply(b); \n return String.valueOf(c); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (108, 96, 1, 'public static String multiply(String num1, String num2) {\n BigInteger n1 = new BigInteger(num1);\n BigInteger n2 = new BigInteger(num2);\n BigInteger n3 = n1.multiply(n2);\n return n3.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (39, 88, 1, 'public static int firstMissingPositive(int[] nums) {
int n = nums.length;
boolean[] seen = new boolean[n + 1];
 // Array for lookup 
 // Mark the elements from nums in the lookup array 
 for (int num : nums) {
 if (num > 0 && num <= n) {
 seen[num] = true;
 }
 }
 // Iterate through integers 1 to n 
 // return smallest missing positive integer 
 for (int i = 1; i <= n; i++) { 
 if (!seen[i]) { 
 return i; 
 }
} 
// If seen contains all elements 1 to n 
// the smallest missing positive number is n + 1 
 return n + 1; 
 }', NULL);
INSERT INTO schema_problem.solution_code VALUES (109, 96, 1, 'public static String multiply(String num1, String num2) { \n int[] num = new int[num1.length()+num2.length()]; \n int len1 = num1.length(), len2 = num2.length(); \n for(int i=len1-1;i>=0;i--){ \n for(int j=len2-1;j>=0;j--){ \n int temp = (num1.charAt(i)-''0'')*(num2.charAt(j)-''0''); \n num[i+j] += (temp+num[i+j+1])/10; \n num[i+j+1] = (num[i+j+1]+temp)%10; \n } \n } \n StringBuilder sb = new StringBuilder(); \n for(int i: num) if(sb.length()>0||i>0)  sb.append(i); \n return (sb.length()==0)?\"0\":sb.toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (135, 100, 1, 'public static boolean isPalindrome(int x) { 
 if (x < 0) { 
 return false; 
 } 
 int reverse = 0; 
 int xcopy = x; 
 while (x > 0) { 
 reverse = (reverse * 10) + (x % 10); 
 x /= 10; 
 } 
 return reverse == xcopy; 
 }', NULL);
INSERT INTO schema_problem.solution_code VALUES (135, 100, 2, 'bool isPalindrome(int x){ 
 if(x<0 || x!=0 && x%10 ==0 ) return false; 
 int check=0; 
 while(x>check){ 
 check = check*10 + x%10; 
 x/=10; 
 } 
 return (x==check || x==check/10); 
 }', NULL);
INSERT INTO schema_problem.solution_code VALUES (110, 96, 1, 'public static String multiply(String nums1, String nums2) { \n if(nums1.equals("0") || nums2.equals("0")) return "0"; \n if(nums1.equals("1")) return nums2; \n if(nums2.equals("1")) return nums1; \n int arr[]=new int[nums1.length()+nums2.length()]; \n for(int i=nums1.length()-1;i>=0;i--){ \n for(int j=nums2.length()-1;j>=0;j--){ \n int prod=(nums1.charAt(i)-''0'')*(nums2.charAt(j)-''0''); \n prod+=arr[i+j+1]; \n arr[i+j+1]=prod%10; \n arr[i+j]+=prod/10; \n } \n } \n StringBuilder ans=new StringBuilder(); \n for(int i=0;i<arr.length;i++){ \n if(ans.length()==0 && arr[i]==0) continue; \n ans.append(arr[i]); \n } \n return ans.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (112, 96, 1, 'public static String multiply(String num1, String num2) { \n int[] num = new int[num1.length()+num2.length()]; \n int len1 = num1.length(), len2 = num2.length(); \n for(int i=len1-1;i>=0;i--){ \n for(int j=len2-1;j>=0;j--){ \n int temp = (num1.charAt(i)-''0'')*(num2.charAt(j)-''0''); \n num[i+j] += (temp+num[i+j+1])/10; \n num[i+j+1] = (num[i+j+1]+temp)%10; \n } \n } \n StringBuilder sb = new StringBuilder(); \n for(int i: num) if(sb.length()>0||i>0)  sb.append(i); \n return (sb.length()==0)?\"0\":sb.toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (113, 96, 1, 'public static String multiply(String a, String b) { \n if (a.equals(\"0\") || b.equals(\"0\")) { \n return \"0\"; \n } \n int m = a.length() - 1, n = b.length() - 1, carry = 0; \n String product = \"\"; \n for (int i = 0; i <= m + n || carry != 0; ++i) { \n for (int j = Math.max(0, i - n); j <= Math.min(i, m); ++j) { \n carry += (a.charAt(m - j) - ''0'') * (b.charAt(n - i + j) - ''0''); \n } \n product += (char)(carry % 10 + ''0''); \n carry /= 10; \n } \n return new StringBuilder(product).reverse().toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (114, 96, 1, 'public static String multiply(String nums1, String nums2) { \n if(nums1.equals("0") || nums2.equals("0")) return "0"; \n if(nums1.equals("1")) return nums2; \n if(nums2.equals("1")) return nums1; \n int arr[]=new int[nums1.length()+nums2.length()]; \n for(int i=nums1.length()-1;i>=0;i--){ \n for(int j=nums2.length()-1;j>=0;j--){ \n int prod=(nums1.charAt(i)-''0'')*(nums2.charAt(j)-''0''); \n prod+=arr[i+j+1]; \n arr[i+j+1]=prod%10; \n arr[i+j]+=prod/10; \n } \n } \n StringBuilder ans=new StringBuilder(); \n for(int i=0;i<arr.length;i++){ \n if(ans.length()==0 && arr[i]==0) continue; \n ans.append(arr[i]); \n } \n return ans.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (115, 96, 1, 'public static String multiply(String num1, String num2) { \n int[] num = new int[num1.length()+num2.length()]; \n int len1 = num1.length(), len2 = num2.length(); \n for(int i=len1-1;i>=0;i--){ \n for(int j=len2-1;j>=0;j--){ \n int temp = (num1.charAt(i)-''0'')*(num2.charAt(j)-''0''); \n num[i+j] += (temp+num[i+j+1])/10; \n num[i+j+1] = (num[i+j+1]+temp)%10; \n } \n } \n StringBuilder sb = new StringBuilder(); \n for(int i: num) if(sb.length()>0||i>0)  sb.append(i); \n return (sb.length()==0)?\"0\":sb.toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (116, 96, 1, 'public static String multiply(String num1, String num2) { \n return String.valueOf((new java.math.BigInteger(num1)).multiply(new java.math.BigInteger(num2))); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (117, 96, 1, 'public static String multiply(String num1, String num2) {\n BigInteger n1 = new BigInteger(num1);\n BigInteger n2 = new BigInteger(num2);\n BigInteger n3 = n1.multiply(n2);\n return n3.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (98, 96, 1, 'public static String multiply(String num1, String num2) {\n BigInteger n1 = new BigInteger(num1);\n BigInteger n2 = new BigInteger(num2);\n BigInteger n3 = n1.multiply(n2);\n return n3.toString();\n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (99, 96, 1, 'public static String multiply(String num1, String num2) { \n return String.valueOf((new java.math.BigInteger(num1)).multiply(new java.math.BigInteger(num2))); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (111, 96, 1, 'public static String multiply(String a, String b) { \n if (a.equals(\"0\") || b.equals(\"0\")) { \n return \"0\"; \n } \n int m = a.length() - 1, n = b.length() - 1, carry = 0; \n String product = \"\"; \n for (int i = 0; i <= m + n || carry != 0; ++i) { \n for (int j = Math.max(0, i - n); j <= Math.min(i, m); ++j) { \n carry += (a.charAt(m - j) - ''0'') * (b.charAt(n - i + j) - ''0''); \n } \n product += (char)(carry % 10 + ''0''); \n carry /= 10; \n } \n return new StringBuilder(product).reverse().toString(); \n }', NULL);
INSERT INTO schema_problem.solution_code VALUES (127, 96, 1, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', NULL);
INSERT INTO schema_problem.solution_code VALUES (128, 96, 1, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', NULL);
INSERT INTO schema_problem.solution_code VALUES (129, 96, 1, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', NULL);
INSERT INTO schema_problem.solution_code VALUES (133, 96, 1, 'public static String multiply(String num1, String num2) {int m = num1.length(), n = num2.length();int[] pos = new int[m + n];for(int i = m - 1; i >= 0; i--) {for(int j = n - 1; j >= 0; j--) {int mul = (num1.charAt(i) - ''0'') * (num2.charAt(j) - ''0'');int p1 = i + j, p2 = i + j + 1; int sum = mul + pos[p2];pos[p1] += sum / 10;pos[p2] = (sum) % 10;}} StringBuilder sb = new StringBuilder();for(int p : pos) if(!(sb.length() == 0 && p == 0))sb.append(p);return sb.length() == 0 ? "0" : sb.toString();}', NULL);
INSERT INTO schema_problem.solution_code VALUES (163, 112, 1, '//Test 

public static int canCompleteCircuit(int[] gas, int[] cost) { 
 int len = gas.length; 
 int[] diff = new int[len]; 
 int to = 0; 
 for(int i = 0;i < len;i++){ 
 diff[i] += (gas[i]-cost[i]); 
 to += diff[i]; 
 } 
 if(to < 0){ 
 return -1; 
 } 
 int index = 0; 
 to = 0; 
 for(int i = 0;i < len;i++){ 
 to += diff[i]; 
 if(to < 0){ 
 index = i+1; 
 to = 0; 
 } 
 } 
 return index; 
 }', NULL);
INSERT INTO schema_problem.solution_code VALUES (163, 112, 2, '//Test 

int canCompleteCircuit(int* gas, int gasSize, int* cost, int costSize){ 
 int gas_tank = 0, start_index = 0, sum = 0; 
 for (int i=0; i<costSize; i++) { 
 sum += gas[i] - cost[i]; 
 gas_tank += gas[i] - cost[i]; 
 if (gas_tank < 0) { 
 start_index = i+1; 
 gas_tank = 0; 
 } 
} 
 return sum < 0 ? -1 : start_index; 
 }', NULL);
INSERT INTO schema_problem.solution_code VALUES (172, 112, 1, '    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        int[] diff = new int[len];
        int to = 0;
        for (int i = 0; i < len; i++) {
            diff[i] += (gas[i] - cost[i]);
            to += diff[i];
        }
        if (to < 0) {
            return -1;
        }
        int index = 0;
        to = 0;
        for (int i = 0; i < len; i++) {
            to += diff[i];
            if (to < 0) {
                index = i + 1;
                to = 0;
            }
        }
        return index;
    }', 215);
INSERT INTO schema_problem.solution_code VALUES (173, 112, 1, '    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        int[] diff = new int[len];
        int to = 0;
        for (int i = 0; i < len; i++) {
            diff[i] += (gas[i] - cost[i]);
            to += diff[i];
        }
        if (to < 0) {
            return -1;
        }
        int index = 0;
        to = 0;
        for (int i = 0; i < len; i++) {
            to += diff[i];
            if (to < 0) {
                index = i + 1;
                to = 0;
            }
        }
        return index;
    }', 215);


--
-- TOC entry 5247 (class 0 OID 17133)
-- Dependencies: 250
-- Data for Name: solution_vote; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.solution_vote VALUES (3, 100);
INSERT INTO schema_problem.solution_vote VALUES (1, 100);


--
-- TOC entry 5241 (class 0 OID 16887)
-- Dependencies: 244
-- Data for Name: test_case; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (318, 112, '[{"name":"gas","type":"ARR_INT","value":[1,2,3,4,5],"noDimension":1},{"name":"cost","type":"ARR_INT","value":[3,4,5,1,2],"noDimension":1}]', '3', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (319, 112, '[{"name":"gas","type":"ARR_INT","value":[2,3,4],"noDimension":1},{"name":"cost","type":"ARR_INT","value":[3,4,3],"noDimension":1}]', '-1', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (320, 112, '[{"name":"gas","type":"ARR_INT","value":[1,2,3,4,5],"noDimension":1},{"name":"gasSize","type":"INT","value":5},{"name":"cost","type":"ARR_INT","value":[3,4,5,1,2],"noDimension":1},{"name":"costSize","type":"INT","value":5}]', '3', true, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (321, 112, '[{"name":"gas","type":"ARR_INT","value":[2,3,4],"noDimension":1},{"name":"gasSize","type":"INT","value":3},{"name":"cost","type":"ARR_INT","value":[3,4,3],"noDimension":1},{"name":"costSize","type":"INT","value":3}]', '-1', true, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (186, 100, '[{"name":"x","type":"INT","value":121}]', 'true', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (187, 100, '[{"name":"x","type":"INT","value":-121}]', 'false', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (188, 100, '[{"name":"x","type":"INT","value":10}]', 'false', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (189, 100, '[{"name":"x","type":"INT","value":13000}]', 'false', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (190, 100, '[{"name":"x","type":"INT","value":151}]', 'true', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (191, 100, '[{"name":"x","type":"INT","value":1221}]', 'true', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (192, 100, '[{"name":"x","type":"INT","value":0}]', 'true', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (193, 100, '[{"name":"x","type":"INT","value":9}]', 'true', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (194, 100, '[{"name":"x","type":"INT","value":30213}]', 'false', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (195, 100, '[{"name":"x","type":"INT","value":121}]', 'true', true, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (196, 100, '[{"name":"x","type":"INT","value":-121}]', 'false', true, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (197, 100, '[{"name":"x","type":"INT","value":10}]', 'false', true, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (198, 100, '[{"name":"x","type":"INT","value":13000}]', 'false', false, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (199, 100, '[{"name":"x","type":"INT","value":151}]', 'true', false, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (200, 100, '[{"name":"x","type":"INT","value":1221}]', 'true', false, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (201, 100, '[{"name":"x","type":"INT","value":0}]', 'true', false, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (202, 100, '[{"name":"x","type":"INT","value":9}]', 'true', false, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (203, 100, '[{"name":"x","type":"INT","value":30213}]', 'false', false, 2);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (2, 25, '[
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
    ]', '[1,2]', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (3, 25, '[
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
    ]', '[0,1]', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (4, 25, '[
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
    ]', '[0,3]', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (5, 25, '[
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
    ]', '[0,2]', false, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (1, 25, '[
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
    ]', '[0,1]', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (22, 62, '[{"name": "nums","type": "ARR_INT","value": [1,0,-1,0,-2,2]},{"name": "target","type": "INT","value": 0}]', '[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (23, 62, '[{"name": "nums","type": "ARR_INT","value": [2,2,2,2,2]},{"name": "target","type": "INT","value": 8}]', '[[2,2,2,2]]', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (81, 88, '[{"name":"nums","type":"ARR_INT","value":[1,2,0]}]', '3', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (82, 88, '[{"name":"nums","type":"ARR_INT","value":[3,4,-1,1]}]', '2', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (83, 88, '[{"name":"nums","type":"ARR_INT","value":[7,8,9,11,12]}]', '1', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (166, 98, '[{"name":"word1","type":"STRING","value":"horse"},{"name":"word2","type":"STRING","value":"ros"}]', '3', true, 1);
INSERT INTO schema_problem.test_case OVERRIDING SYSTEM VALUE VALUES (167, 98, '[{"name":"word1","type":"STRING","value":"intention"},{"name":"word2","type":"STRING","value":"execution"}]', '5', true, 1);


--
-- TOC entry 5232 (class 0 OID 16747)
-- Dependencies: 235
-- Data for Name: user_favourite; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.user_favourite VALUES (1, 96);
INSERT INTO schema_problem.user_favourite VALUES (3, 96);
INSERT INTO schema_problem.user_favourite VALUES (3, 98);
INSERT INTO schema_problem.user_favourite VALUES (1, 25);
INSERT INTO schema_problem.user_favourite VALUES (1, 112);


--
-- TOC entry 5227 (class 0 OID 16686)
-- Dependencies: 230
-- Data for Name: language; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--

INSERT INTO schema_setting.language OVERRIDING SYSTEM VALUE VALUES (1, 'Java', '2025-01-23 22:00:00', 1, NULL, NULL);
INSERT INTO schema_setting.language OVERRIDING SYSTEM VALUE VALUES (2, 'C', '2025-01-23 22:00:00', 1, NULL, NULL);
INSERT INTO schema_setting.language OVERRIDING SYSTEM VALUE VALUES (35, 'Javascript', '2025-03-12 19:44:13.64928', 2, '2025-03-12 22:37:56.541589', 2);


--
-- TOC entry 5223 (class 0 OID 16650)
-- Dependencies: 226
-- Data for Name: skill; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--

INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (1, 'Array', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (2, 'String', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (3, 'Brute Force', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (4, 'Two-pass Hash Table', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (5, 'One-pass Hash Table', 'FUNDAMENTAL', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (6, 'Dynamic Programming', 'ADVANCED', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (7, 'Backtracking', 'ADVANCED', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (8, 'Trie', 'ADVANCED', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (9, 'Hash Table', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (10, 'Depth-First Search', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (11, 'Math', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (13, 'Tree', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (14, 'Binary Tree', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (15, 'Greedy', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (16, 'Bredth-First Search', 'INTERMEDIATE', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.skill OVERRIDING SYSTEM VALUE VALUES (37, 'Three Dimensional Array', 'ADVANCED', '2025-03-12 20:09:43.2358', 2, '2025-03-12 22:38:27.149194', 2);


--
-- TOC entry 5225 (class 0 OID 16668)
-- Dependencies: 228
-- Data for Name: topic; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--

INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (1, 'Companies', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (2, 'Array', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (3, 'Loop', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (4, 'Recursion', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (5, 'List', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (6, 'String', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (7, 'Map', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (8, 'Object', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (9, 'Math', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (16, 'Interview', '2025-02-14 16:29:07.051563', 70, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (17, 'Two Pointers', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (18, 'Sorting', '2025-01-02 12:56:21.718047', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (19, 'Greedy', '2025-02-14 16:29:07.051563', 1, NULL, NULL);
INSERT INTO schema_setting.topic OVERRIDING SYSTEM VALUE VALUES (22, 'Top Interview Question', '2025-03-12 19:44:24.605115', 2, '2025-03-12 22:38:07.21666', 2);


--
-- TOC entry 5229 (class 0 OID 16704)
-- Dependencies: 232
-- Data for Name: notification; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--

INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (1, 76, 'Welcome to Kodeholik', NULL, '2025-02-28 08:43:02.924344', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (4, 22, 'There is a exam that will start on 05/03/2025, 18:20', '', '2025-03-05 18:00:35.089178', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (2, 5, 'There is a exam that will start on 05/03/2025, 18:20', '', '2025-03-05 18:00:35.084413', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (3, 1, 'There is a exam that will start on 05/03/2025, 18:20', '', '2025-03-05 18:00:35.081777', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (5, 5, 'There is a exam that will start on 05/03/2025, 18:23', '', '2025-03-05 18:22:19.470739', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (6, 1, 'There is a exam that will start on 05/03/2025, 18:23', '', '2025-03-05 18:22:19.470739', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (7, 22, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:08:04.388452', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (8, 1, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:08:04.378431', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (9, 5, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:08:04.382096', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (10, 1, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:08:54.22793', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (11, 5, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:08:54.231252', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (12, 22, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:08:54.234446', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (13, 1, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:09:24.229312', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (14, 5, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:09:24.231418', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (15, 22, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:09:24.233522', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (16, 5, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:12:18.335594', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (17, 1, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:12:18.333488', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (18, 22, 'There is a exam that will start on 05/03/2025, 19:23', '', '2025-03-05 19:12:18.339619', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (20, 5, 'There is a exam that will start on 05/03/2025, 19:43', '', '2025-03-05 19:18:38.445651', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (19, 1, 'There is a exam that will start on 05/03/2025, 19:43', '', '2025-03-05 19:18:38.444598', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (21, 22, 'There is a exam that will start on 05/03/2025, 19:43', '', '2025-03-05 19:18:38.452445', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (23, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:13:59.360411', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (22, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:13:59.360411', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (24, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:13:59.378235', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (25, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:18:26.066228', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (26, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:18:26.068329', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (27, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:18:26.072563', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (28, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:19:00.875096', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (29, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:19:00.878856', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (30, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:19:00.883163', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (31, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:19:25.860073', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (32, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:19:25.86222', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (33, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:19:25.864361', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (34, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:20:05.866426', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (35, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:20:05.868553', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (36, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:20:05.872248', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (39, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:21:30.003573', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (38, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:21:30.007791', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (37, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:21:30.000491', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (40, 1, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:21:44.984954', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (41, 5, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:21:44.987067', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (42, 22, 'There is a exam that will start on 06/03/2025, 03:43', '', '2025-03-06 03:21:44.990288', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (45, 1, 'There is a exam that will start on 06/03/2025, 11:03', '', '2025-03-06 11:02:09.460433', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (44, 5, 'There is a exam that will start on 06/03/2025, 11:03', '', '2025-03-06 11:02:09.464765', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (43, 22, 'There is a exam that will start on 06/03/2025, 11:03', '', '2025-03-06 11:02:09.467936', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (46, 5, 'There is a exam that will start on 06/03/2025, 13:30', '', '2025-03-06 13:28:22.554609', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (48, 1, 'There is a exam that will start on 06/03/2025, 13:30', '', '2025-03-06 13:28:22.550098', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (47, 22, 'There is a exam that will start on 06/03/2025, 13:30', '', '2025-03-06 13:28:22.560221', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (51, 1, 'There is a exam that will start on 06/03/2025, 19:30', '', '2025-03-06 19:25:24.284365', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (49, 22, 'There is a exam that will start on 06/03/2025, 19:30', '', '2025-03-06 19:25:24.293124', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (50, 5, 'There is a exam that will start on 06/03/2025, 19:30', '', '2025-03-06 19:25:24.288074', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (52, 1, 'There is a exam that will start on 06/03/2025, 19:46', '', '2025-03-06 19:39:32.957063', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (53, 1, 'There is a exam that will start on 06/03/2025, 20:00', '', '2025-03-06 19:53:23.584354', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (54, 1, 'There is a exam that will start on 06/03/2025, 20:05', '', '2025-03-06 20:04:43.564764', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (55, 1, 'There is a exam that will start on 06/03/2025, 22:53', '', '2025-03-06 22:51:21.809764', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (56, 1, 'There is a exam that will start on 07/03/2025, 08:00', '', '2025-03-07 07:57:20.356944', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (57, 1, 'There is a exam that will start on 07/03/2025, 08:00', '', '2025-03-07 07:58:49.615063', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (58, 2, 'Welcome to Kodeholik', NULL, '2025-03-12 04:56:17.840246', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (59, 2, 'Welcome to Kodeholik', NULL, '2025-03-12 05:20:14.00119', 'SYSTEM');
INSERT INTO schema_user.notification OVERRIDING SYSTEM VALUE VALUES (60, 2, 'Welcome to Kodeholik', NULL, '2025-03-12 20:08:11.169127', 'SYSTEM');


--
-- TOC entry 5249 (class 0 OID 17244)
-- Dependencies: 252
-- Data for Name: transaction; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--



--
-- TOC entry 5221 (class 0 OID 16570)
-- Dependencies: 224
-- Data for Name: users; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--

INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (5, 'HngThng', 'Dang Hong Thang', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'thangdh1557@gmail.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (83, 'mastadmin2', 'Trần Hải Bằng 2', '$2a$10$Tna5NDaL6phdXyNOWB82bestcJOHrSRlJgTejhVRNEwGqJHmchzby', 'kodeholik2@gmail.com', 'ADMIN', 'ACTIVATED', '2025-03-12', 'kodeholik-avatar-image-205945af-a822-4a3c-8fa9-8f6c4b0eb067');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (78, 'mastadmin3', 'Trần Hải Bằng ', '$2a$10$i8VGQMFnPvh48KYB4FYJYePG0SXVfwND2oYxgsgQO3TsCvSlRbMZW', 'kodeholik@gmail.com', 'ADMIN', 'NOT_ACTIVATED', '2025-03-12', 'kodeholik-avatar-image-6d36d1b8-b4f1-44a5-9e85-debb6e3d39b6');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (84, 'NgVanA', 'Nguyễn Văn A', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'ngvana@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (6, 'Jasmine Milk', 'Vu Tuan Hung', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'hungvt321@gmail.com', 'STUDENT', 'BANNED', '2024-10-12', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (67, 'binhtq', 'Tran Quoc Binh', '$2a$10$P2Y3FTKGIoMtXqNfLYQ95OjXfplOACRVoJc/tcXdzdVqCcfmjCSf2', 'binhtq@gmail.com', 'STUDENT', 'ACTIVATED', '2025-02-13', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (68, 'baotq', 'Tran Quoc Bao', '$2a$10$4T5KVE1i8E3ExtT9uRX1s.IL5d6P.VnGym8oXlt/J7kT.YY8PEOUi', 'baotq@gmail.com', 'STUDENT', 'ACTIVATED', '2025-02-13', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (2, 'Phong', 'Pham Duy Phong', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'phongk72tp@gmail.com', 'ADMIN', 'ACTIVATED', '2024-05-01', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (70, 'haitq', 'Tran Quoc Hai', '$2a$10$DqXH5YDKXQD35XrztK1SeudCH5g1boI1z22pUuSPMYgxqaQ6vSlHa', '12', 'STUDENT', 'ACTIVATED', '2025-02-13', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (85, 'TrThiB', 'Trần Thị B', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'tranthib@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (22, '27 - Trần Hải Bằng', '27 - Trần Hải Bằng', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'bangthhe170871@fpt.edu.vn', 'TEACHER', 'ACTIVATED', '2025-02-10', 'https://lh3.googleusercontent.com/a/ACg8ocLUGVSI7vY8J1U-dedYf6BNl_pub8sHys0YFcbHGUSToW5ihw=s96-c');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (86, 'LeVanC', 'Lê Văn C', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'levanc@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (87, 'PhThiD', 'Phạm Thị D', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'phamthid@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (88, 'NgocAnh', 'Ngọc Anh', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'ngocanh@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (89, 'DucMinh', 'Đức Minh', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'ducminh@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (76, 'Near Me', 'Near Me', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'basihamedical@gmail.com', 'EXAMINER', 'ACTIVATED', '2025-02-28', 'https://lh3.googleusercontent.com/a/ACg8ocIArfWVqGfDK0hMAdne8ZWCuGRZvwnHDsDhQFIDdU3f2JVwfA=s96-c');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (90, 'HoaiNam', 'Hoài Nam', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'hoainam@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (91, 'BaoChau', 'Bảo Châu', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'baochau@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (3, 'Thai', 'Pham Hong Thai', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'thaiph@gmail.com', 'STUDENT', 'ACTIVATED', '2024-06-02', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (4, 'Duy', 'Dang Nguyen Quang Duy', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'duydnq123@gmail.com', 'STUDENT', 'ACTIVATED', '2024-07-03', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (92, 'ThanhTung', 'Thanh Tùng', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'thanhtung@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (93, 'MinhHanh', 'Minh Hạnh', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'minhhanh@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (94, 'HaiYen', 'Hải Yến', '$2y$10$Tq5qcPgXnPftWwm0ko54DOelkO2yLc6K6o7b63gQu0wdSM4zRUiwa', 'haiyen@example.com', 'STUDENT', 'ACTIVATED', '2024-09-06', 'kodeholik-problem-image-2f3ce180-2409-43fc-85eb-d427f560cc47');
INSERT INTO schema_user.users OVERRIDING SYSTEM VALUE VALUES (1, 'Mast', 'Trần Hải Bằng', '$2a$10$Tmf5mi9qG.y3vZRLBOF6ueRd5OXE01grzQJKiJ6ZxgXr/zVz.xeiC', 'tranhaibang665@gmail.com', 'STUDENT', 'ACTIVATED', '2024-03-20', 'kodeholik-avatar-image-15de58ce-527d-4355-b479-d136b79c5571');


--
-- TOC entry 5281 (class 0 OID 0)
-- Dependencies: 266
-- Name: chapter_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.chapter_id_seq', 5, true);


--
-- TOC entry 5282 (class 0 OID 0)
-- Dependencies: 269
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_id_seq', 10, true);


--
-- TOC entry 5283 (class 0 OID 0)
-- Dependencies: 271
-- Name: course_rating_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_rating_id_seq', 6, true);


--
-- TOC entry 5284 (class 0 OID 0)
-- Dependencies: 275
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.lesson_id_seq', 34, true);


--
-- TOC entry 5285 (class 0 OID 0)
-- Dependencies: 247
-- Name: discussion_id_seq; Type: SEQUENCE SET; Schema: schema_discussion; Owner: postgres
--

SELECT pg_catalog.setval('schema_discussion.discussion_id_seq', 78, true);


--
-- TOC entry 5286 (class 0 OID 0)
-- Dependencies: 259
-- Name: exam_id_seq; Type: SEQUENCE SET; Schema: schema_exam; Owner: postgres
--

SELECT pg_catalog.setval('schema_exam.exam_id_seq', 58, true);


--
-- TOC entry 5287 (class 0 OID 0)
-- Dependencies: 233
-- Name: problem_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_id_seq', 112, true);


--
-- TOC entry 5288 (class 0 OID 0)
-- Dependencies: 254
-- Name: problem_input_parameter_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_input_parameter_id_seq', 294, true);


--
-- TOC entry 5289 (class 0 OID 0)
-- Dependencies: 245
-- Name: problem_submission_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_submission_id_seq', 232, true);


--
-- TOC entry 5290 (class 0 OID 0)
-- Dependencies: 238
-- Name: problemsolution_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problemsolution_id_seq', 173, true);


--
-- TOC entry 5291 (class 0 OID 0)
-- Dependencies: 241
-- Name: problemtemplate_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problemtemplate_id_seq', 161, true);


--
-- TOC entry 5292 (class 0 OID 0)
-- Dependencies: 243
-- Name: testcase_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.testcase_id_seq', 321, true);


--
-- TOC entry 5293 (class 0 OID 0)
-- Dependencies: 229
-- Name: language_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.language_id_seq', 35, true);


--
-- TOC entry 5294 (class 0 OID 0)
-- Dependencies: 225
-- Name: skill_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.skill_id_seq', 37, true);


--
-- TOC entry 5295 (class 0 OID 0)
-- Dependencies: 227
-- Name: topic_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.topic_id_seq', 22, true);


--
-- TOC entry 5296 (class 0 OID 0)
-- Dependencies: 231
-- Name: notification_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.notification_id_seq', 60, true);


--
-- TOC entry 5297 (class 0 OID 0)
-- Dependencies: 251
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.transaction_id_seq', 1, false);


--
-- TOC entry 5298 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.users_id_seq', 94, true);


SET default_tablespace = '';

--
-- TOC entry 4981 (class 2606 OID 18846)
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (id);


--
-- TOC entry 4985 (class 2606 OID 18848)
-- Name: course_comment course_comment_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_pkey PRIMARY KEY (course_id, comment_id);


--
-- TOC entry 4983 (class 2606 OID 18850)
-- Name: course course_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- TOC entry 4987 (class 2606 OID 18852)
-- Name: course_rating course_rating_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_rating
    ADD CONSTRAINT course_rating_pkey PRIMARY KEY (id);


--
-- TOC entry 4989 (class 2606 OID 18854)
-- Name: course_topic course_topic_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT course_topic_pkey PRIMARY KEY (course_id, topic_id);


--
-- TOC entry 4991 (class 2606 OID 18856)
-- Name: course_user course_user_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_pkey PRIMARY KEY (course_id, user_id);


--
-- TOC entry 4993 (class 2606 OID 18858)
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- TOC entry 4995 (class 2606 OID 18860)
-- Name: lesson_problem lesson_problem_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_pkey PRIMARY KEY (lesson_id, problem_id);


--
-- TOC entry 4997 (class 2606 OID 18862)
-- Name: user_lesson_progress user_lesson_progress_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.user_lesson_progress
    ADD CONSTRAINT user_lesson_progress_pkey PRIMARY KEY (user_id, lesson_id);


--
-- TOC entry 4953 (class 2606 OID 17107)
-- Name: comment_vote comment_vote_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment_vote
    ADD CONSTRAINT comment_vote_pkey PRIMARY KEY (user_id, comment_id);


--
-- TOC entry 4951 (class 2606 OID 17056)
-- Name: comment discussion_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_pkey PRIMARY KEY (id);


--
-- TOC entry 4969 (class 2606 OID 18739)
-- Name: exam exam_code_key; Type: CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam
    ADD CONSTRAINT exam_code_key UNIQUE (code);


--
-- TOC entry 4979 (class 2606 OID 18727)
-- Name: exam_language_support exam_language_support_pkey; Type: CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_language_support
    ADD CONSTRAINT exam_language_support_pkey PRIMARY KEY (exam_id, language_id);


--
-- TOC entry 4973 (class 2606 OID 18676)
-- Name: exam_participant exam_participant_pkey; Type: CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_participant
    ADD CONSTRAINT exam_participant_pkey PRIMARY KEY (exam_id, participant_id);


--
-- TOC entry 4971 (class 2606 OID 18654)
-- Name: exam exam_pkey; Type: CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam
    ADD CONSTRAINT exam_pkey PRIMARY KEY (id);


--
-- TOC entry 4975 (class 2606 OID 18692)
-- Name: exam_problem exam_problem_pkey; Type: CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_problem
    ADD CONSTRAINT exam_problem_pkey PRIMARY KEY (exam_id, problem_id);


--
-- TOC entry 4977 (class 2606 OID 18707)
-- Name: exam_submission exam_submission_pkey; Type: CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_submission
    ADD CONSTRAINT exam_submission_pkey PRIMARY KEY (exam_id, participant_id, problem_id);


--
-- TOC entry 4967 (class 2606 OID 18619)
-- Name: language_support language_support_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.language_support
    ADD CONSTRAINT language_support_pkey PRIMARY KEY (problem_id, language_id);


SET default_tablespace = kodeholik_problem_data;

--
-- TOC entry 4943 (class 2606 OID 18360)
-- Name: solution_code pk; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres; Tablespace: kodeholik_problem_data
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT pk PRIMARY KEY (solution_id, language_id);


SET default_tablespace = '';

--
-- TOC entry 4963 (class 2606 OID 17624)
-- Name: problem_comment problem_comment_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_comment
    ADD CONSTRAINT problem_comment_pkey PRIMARY KEY (problem_id, comment_id);


--
-- TOC entry 4961 (class 2606 OID 17560)
-- Name: problem_input_parameter problem_input_parameter_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_input_parameter
    ADD CONSTRAINT problem_input_parameter_pkey PRIMARY KEY (id);


--
-- TOC entry 4933 (class 2606 OID 17397)
-- Name: problem problem_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_pkey PRIMARY KEY (id);


--
-- TOC entry 4965 (class 2606 OID 17640)
-- Name: problem_solution_comment problem_solution_comment_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_comment
    ADD CONSTRAINT problem_solution_comment_pkey PRIMARY KEY (problem_solution_id, comment_id);


--
-- TOC entry 4959 (class 2606 OID 17331)
-- Name: problem_solution_skill problem_solution_skill_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_pkey PRIMARY KEY (problem_solution_id, skill_id);


--
-- TOC entry 4949 (class 2606 OID 17010)
-- Name: problem_submission problem_submission_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_pkey PRIMARY KEY (id);


--
-- TOC entry 4939 (class 2606 OID 16781)
-- Name: problem_skill problemskill_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_pkey PRIMARY KEY (problem_id, skill_id);


--
-- TOC entry 4941 (class 2606 OID 16799)
-- Name: problem_solution problemsolution_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT problemsolution_pkey PRIMARY KEY (id);


--
-- TOC entry 4945 (class 2606 OID 16874)
-- Name: problem_template problemtemplate_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_pkey PRIMARY KEY (id);


--
-- TOC entry 4937 (class 2606 OID 16766)
-- Name: problem_topic problemtopic_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_pkey PRIMARY KEY (problem_id, topic_id);


--
-- TOC entry 4955 (class 2606 OID 17137)
-- Name: solution_vote solution_vote_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_pkey PRIMARY KEY (user_id, solution_id);


--
-- TOC entry 4947 (class 2606 OID 16893)
-- Name: test_case testcase_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT testcase_pkey PRIMARY KEY (id);


--
-- TOC entry 4935 (class 2606 OID 16751)
-- Name: user_favourite userfavourite_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_pkey PRIMARY KEY (user_id, problem_id);


--
-- TOC entry 4927 (class 2606 OID 16692)
-- Name: language language_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_name_key UNIQUE (name);


--
-- TOC entry 4929 (class 2606 OID 16690)
-- Name: language language_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- TOC entry 4919 (class 2606 OID 16656)
-- Name: skill skill_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_name_key UNIQUE (name);


--
-- TOC entry 4921 (class 2606 OID 16654)
-- Name: skill skill_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_pkey PRIMARY KEY (id);


--
-- TOC entry 4923 (class 2606 OID 16674)
-- Name: topic topic_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_name_key UNIQUE (name);


--
-- TOC entry 4925 (class 2606 OID 16672)
-- Name: topic topic_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- TOC entry 4931 (class 2606 OID 16710)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 4957 (class 2606 OID 17250)
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);


--
-- TOC entry 4913 (class 2606 OID 16578)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4915 (class 2606 OID 16574)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4917 (class 2606 OID 16576)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 5056 (class 2606 OID 18863)
-- Name: chapter chapter_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 5057 (class 2606 OID 18868)
-- Name: chapter chapter_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5058 (class 2606 OID 18873)
-- Name: chapter chapter_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5061 (class 2606 OID 18878)
-- Name: course_comment course_comment_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 5059 (class 2606 OID 18883)
-- Name: course course_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5062 (class 2606 OID 18888)
-- Name: course_rating course_rating_course_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_rating
    ADD CONSTRAINT course_rating_course_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5063 (class 2606 OID 18893)
-- Name: course_rating course_rating_user_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_rating
    ADD CONSTRAINT course_rating_user_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5060 (class 2606 OID 18898)
-- Name: course course_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5066 (class 2606 OID 18903)
-- Name: course_user course_user_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON DELETE CASCADE;


--
-- TOC entry 5067 (class 2606 OID 18908)
-- Name: course_user course_user_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON DELETE CASCADE;


--
-- TOC entry 5064 (class 2606 OID 18913)
-- Name: course_topic fk_course; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON DELETE CASCADE;


--
-- TOC entry 5065 (class 2606 OID 18918)
-- Name: course_topic fk_topic; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT fk_topic FOREIGN KEY (topic_id) REFERENCES schema_setting.topic(id) ON DELETE CASCADE;


--
-- TOC entry 5068 (class 2606 OID 18923)
-- Name: lesson lesson_chapter_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES schema_course.chapter(id);


--
-- TOC entry 5069 (class 2606 OID 18928)
-- Name: lesson lesson_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5071 (class 2606 OID 18933)
-- Name: lesson_problem lesson_problem_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id);


--
-- TOC entry 5072 (class 2606 OID 18938)
-- Name: lesson_problem lesson_problem_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5070 (class 2606 OID 18943)
-- Name: lesson lesson_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5073 (class 2606 OID 18948)
-- Name: user_lesson_progress user_lesson_progress_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.user_lesson_progress
    ADD CONSTRAINT user_lesson_progress_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id) ON DELETE CASCADE;


--
-- TOC entry 5074 (class 2606 OID 18953)
-- Name: user_lesson_progress user_lesson_progress_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.user_lesson_progress
    ADD CONSTRAINT user_lesson_progress_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON DELETE CASCADE;


--
-- TOC entry 5030 (class 2606 OID 17113)
-- Name: comment_vote comment_vote_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment_vote
    ADD CONSTRAINT comment_vote_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 5031 (class 2606 OID 17108)
-- Name: comment_vote comment_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment_vote
    ADD CONSTRAINT comment_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 5027 (class 2606 OID 18405)
-- Name: comment discussion_comment_reply_fk; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_comment_reply_fk FOREIGN KEY (comment_reply) REFERENCES schema_discussion.comment(id) NOT VALID;


--
-- TOC entry 5028 (class 2606 OID 17062)
-- Name: comment discussion_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5029 (class 2606 OID 17067)
-- Name: comment discussion_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.comment
    ADD CONSTRAINT discussion_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5045 (class 2606 OID 18657)
-- Name: exam exam_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam
    ADD CONSTRAINT exam_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5054 (class 2606 OID 18728)
-- Name: exam_language_support exam_language_support_exam_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_language_support
    ADD CONSTRAINT exam_language_support_exam_id_fkey FOREIGN KEY (exam_id) REFERENCES schema_exam.exam(id);


--
-- TOC entry 5055 (class 2606 OID 18733)
-- Name: exam_language_support exam_language_support_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_language_support
    ADD CONSTRAINT exam_language_support_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 5047 (class 2606 OID 18677)
-- Name: exam_participant exam_participant_exam_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_participant
    ADD CONSTRAINT exam_participant_exam_id_fkey FOREIGN KEY (exam_id) REFERENCES schema_exam.exam(id);


--
-- TOC entry 5048 (class 2606 OID 18682)
-- Name: exam_participant exam_participant_participant_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_participant
    ADD CONSTRAINT exam_participant_participant_id_fkey FOREIGN KEY (participant_id) REFERENCES schema_user.users(id);


--
-- TOC entry 5049 (class 2606 OID 18693)
-- Name: exam_problem exam_problem_exam_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_problem
    ADD CONSTRAINT exam_problem_exam_id_fkey FOREIGN KEY (exam_id) REFERENCES schema_exam.exam(id);


--
-- TOC entry 5050 (class 2606 OID 18698)
-- Name: exam_problem exam_problem_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_problem
    ADD CONSTRAINT exam_problem_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5051 (class 2606 OID 18708)
-- Name: exam_submission exam_submission_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_submission
    ADD CONSTRAINT exam_submission_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5052 (class 2606 OID 18713)
-- Name: exam_submission exam_submission_submission_id_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_submission
    ADD CONSTRAINT exam_submission_submission_id_fkey FOREIGN KEY (submission_id) REFERENCES schema_problem.problem_submission(id);


--
-- TOC entry 5046 (class 2606 OID 18662)
-- Name: exam exam_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam
    ADD CONSTRAINT exam_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5053 (class 2606 OID 18718)
-- Name: exam_submission fk_exam_participant; Type: FK CONSTRAINT; Schema: schema_exam; Owner: postgres
--

ALTER TABLE ONLY schema_exam.exam_submission
    ADD CONSTRAINT fk_exam_participant FOREIGN KEY (exam_id, participant_id) REFERENCES schema_exam.exam_participant(exam_id, participant_id);


--
-- TOC entry 5013 (class 2606 OID 18399)
-- Name: problem_solution created_fk; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT created_fk FOREIGN KEY (created_by) REFERENCES schema_user.users(id) NOT VALID;


--
-- TOC entry 5037 (class 2606 OID 18387)
-- Name: problem_input_parameter language_fk; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_input_parameter
    ADD CONSTRAINT language_fk FOREIGN KEY (language_id) REFERENCES schema_setting.language(id) NOT VALID;


--
-- TOC entry 5022 (class 2606 OID 18607)
-- Name: test_case language_fk; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT language_fk FOREIGN KEY (language_id) REFERENCES schema_setting.language(id) NOT VALID;


--
-- TOC entry 5043 (class 2606 OID 18625)
-- Name: language_support language_support_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.language_support
    ADD CONSTRAINT language_support_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 5044 (class 2606 OID 18620)
-- Name: language_support language_support_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.language_support
    ADD CONSTRAINT language_support_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5039 (class 2606 OID 17630)
-- Name: problem_comment problem_comment_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_comment
    ADD CONSTRAINT problem_comment_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 5040 (class 2606 OID 17625)
-- Name: problem_comment problem_comment_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_comment
    ADD CONSTRAINT problem_comment_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5005 (class 2606 OID 16737)
-- Name: problem problem_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5038 (class 2606 OID 17561)
-- Name: problem_input_parameter problem_input_parameter_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_input_parameter
    ADD CONSTRAINT problem_input_parameter_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5041 (class 2606 OID 17646)
-- Name: problem_solution_comment problem_solution_comment_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_comment
    ADD CONSTRAINT problem_solution_comment_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- TOC entry 5042 (class 2606 OID 17641)
-- Name: problem_solution_comment problem_solution_comment_problem_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_comment
    ADD CONSTRAINT problem_solution_comment_problem_solution_id_fkey FOREIGN KEY (problem_solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 5035 (class 2606 OID 17332)
-- Name: problem_solution_skill problem_solution_skill_problem_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_problem_solution_id_fkey FOREIGN KEY (problem_solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 5036 (class 2606 OID 17337)
-- Name: problem_solution_skill problem_solution_skill_skill_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES schema_setting.skill(id);


--
-- TOC entry 5024 (class 2606 OID 17021)
-- Name: problem_submission problem_submission_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 5025 (class 2606 OID 17433)
-- Name: problem_submission problem_submission_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5026 (class 2606 OID 17011)
-- Name: problem_submission problem_submission_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 5006 (class 2606 OID 16742)
-- Name: problem problem_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5011 (class 2606 OID 17408)
-- Name: problem_skill problemskill_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5012 (class 2606 OID 16787)
-- Name: problem_skill problemskill_skill_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES schema_setting.skill(id);


--
-- TOC entry 5014 (class 2606 OID 17413)
-- Name: problem_solution problemsolution_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT problemsolution_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5020 (class 2606 OID 16880)
-- Name: problem_template problemtemplate_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 5021 (class 2606 OID 17423)
-- Name: problem_template problemtemplate_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5009 (class 2606 OID 17403)
-- Name: problem_topic problemtopic_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5010 (class 2606 OID 16772)
-- Name: problem_topic problemtopic_topic_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES schema_setting.topic(id);


--
-- TOC entry 5032 (class 2606 OID 17143)
-- Name: solution_vote solution_vote_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_solution_id_fkey FOREIGN KEY (solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 5033 (class 2606 OID 17138)
-- Name: solution_vote solution_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 5016 (class 2606 OID 16857)
-- Name: solution_code solutioncode_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 5017 (class 2606 OID 17418)
-- Name: solution_code solutioncode_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5018 (class 2606 OID 16847)
-- Name: solution_code solutioncode_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_solution_id_fkey FOREIGN KEY (solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 5019 (class 2606 OID 18958)
-- Name: solution_code submission_id_fk; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT submission_id_fk FOREIGN KEY (submission_id) REFERENCES schema_problem.problem_submission(id) NOT VALID;


--
-- TOC entry 5023 (class 2606 OID 17428)
-- Name: test_case testcase_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT testcase_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5015 (class 2606 OID 18394)
-- Name: problem_solution updated_fk; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT updated_fk FOREIGN KEY (updated_by) REFERENCES schema_user.users(id) NOT VALID;


--
-- TOC entry 5007 (class 2606 OID 17398)
-- Name: user_favourite userfavourite_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 5008 (class 2606 OID 16752)
-- Name: user_favourite userfavourite_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 5002 (class 2606 OID 16693)
-- Name: language language_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5003 (class 2606 OID 16698)
-- Name: language language_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4998 (class 2606 OID 16657)
-- Name: skill skill_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4999 (class 2606 OID 16662)
-- Name: skill skill_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5000 (class 2606 OID 16675)
-- Name: topic topic_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5001 (class 2606 OID 16680)
-- Name: topic topic_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 5004 (class 2606 OID 16711)
-- Name: notification notification_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 5034 (class 2606 OID 17251)
-- Name: transaction transaction_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.transaction
    ADD CONSTRAINT transaction_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


-- Completed on 2025-03-17 00:16:41

--
-- PostgreSQL database dump complete
--

