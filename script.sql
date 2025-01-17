--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-01-17 19:07:54

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
-- TOC entry 1011 (class 1247 OID 17362)
-- Name: difficulty_new; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.difficulty_new AS ENUM (
    'EASY',
    'MEDIUM',
    'HARD'
);


ALTER TYPE public.difficulty_new OWNER TO postgres;

--
-- TOC entry 990 (class 1247 OID 17257)
-- Name: contest_status; Type: TYPE; Schema: schema_contest; Owner: postgres
--

CREATE TYPE schema_contest.contest_status AS ENUM (
    'not started',
    'in progress',
    'ended'
);


ALTER TYPE schema_contest.contest_status OWNER TO postgres;

--
-- TOC entry 966 (class 1247 OID 17149)
-- Name: chapter_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.chapter_status AS ENUM (
    'activated',
    'inactivated'
);


ALTER TYPE schema_course.chapter_status OWNER TO postgres;

--
-- TOC entry 945 (class 1247 OID 17027)
-- Name: course_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.course_status AS ENUM (
    'activated',
    'inactivated'
);


ALTER TYPE schema_course.course_status OWNER TO postgres;

--
-- TOC entry 972 (class 1247 OID 17177)
-- Name: lesson_type; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_type AS ENUM (
    'video',
    'text',
    'quiz',
    'assignment'
);


ALTER TYPE schema_course.lesson_type OWNER TO postgres;

--
-- TOC entry 1014 (class 1247 OID 17382)
-- Name: difficulty; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.difficulty AS ENUM (
    'EASY',
    'MEDIUM',
    'HARD'
);


ALTER TYPE schema_problem.difficulty OWNER TO postgres;

--
-- TOC entry 1008 (class 1247 OID 17350)
-- Name: problem_status; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.problem_status AS ENUM (
    'PUBLIC',
    'PRIVATE'
);


ALTER TYPE schema_problem.problem_status OWNER TO postgres;

--
-- TOC entry 939 (class 1247 OID 16996)
-- Name: submission_status; Type: TYPE; Schema: schema_problem; Owner: postgres
--

CREATE TYPE schema_problem.submission_status AS ENUM (
    'accepted',
    'wrong answer',
    'overflow',
    'ACCEPTED'
);


ALTER TYPE schema_problem.submission_status OWNER TO postgres;

--
-- TOC entry 897 (class 1247 OID 16580)
-- Name: level; Type: TYPE; Schema: schema_setting; Owner: postgres
--

CREATE TYPE schema_setting.level AS ENUM (
    'easy',
    'medium',
    'hard'
);


ALTER TYPE schema_setting.level OWNER TO postgres;

--
-- TOC entry 984 (class 1247 OID 17230)
-- Name: transaction_status; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.transaction_status AS ENUM (
    'success',
    'failed'
);


ALTER TYPE schema_user.transaction_status OWNER TO postgres;

--
-- TOC entry 981 (class 1247 OID 17224)
-- Name: transaction_type; Type: TYPE; Schema: schema_user; Owner: postgres
--

CREATE TYPE schema_user.transaction_type AS ENUM (
    'topup',
    'withdraw'
);


ALTER TYPE schema_user.transaction_type OWNER TO postgres;

--
-- TOC entry 1020 (class 1247 OID 17494)
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
-- TOC entry 1017 (class 1247 OID 17504)
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
-- TOC entry 264 (class 1259 OID 17264)
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
-- TOC entry 265 (class 1259 OID 17281)
-- Name: contest_coworker; Type: TABLE; Schema: schema_contest; Owner: postgres; Tablespace: kodeholik_contest_data
--

CREATE TABLE schema_contest.contest_coworker (
    contest_id integer NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE schema_contest.contest_coworker OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 17263)
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
-- TOC entry 266 (class 1259 OID 17296)
-- Name: contest_participant; Type: TABLE; Schema: schema_contest; Owner: postgres; Tablespace: kodeholik_contest_data
--

CREATE TABLE schema_contest.contest_participant (
    contest_id integer NOT NULL,
    user_id integer NOT NULL,
    point double precision
);


ALTER TABLE schema_contest.contest_participant OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 17311)
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
-- TOC entry 257 (class 1259 OID 17154)
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
-- TOC entry 256 (class 1259 OID 17153)
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
-- TOC entry 259 (class 1259 OID 17186)
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
-- TOC entry 258 (class 1259 OID 17185)
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
-- TOC entry 260 (class 1259 OID 17208)
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
-- Name: discussion; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.discussion (
    id integer NOT NULL,
    course_id integer,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    upvote integer,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_discussion.discussion OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 17081)
-- Name: discussion_comment; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.discussion_comment (
    id integer NOT NULL,
    discussion_id integer NOT NULL,
    comment text NOT NULL,
    comment_reply_id integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL
);


ALTER TABLE schema_discussion.discussion_comment OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 17080)
-- Name: discussion_comment_id_seq; Type: SEQUENCE; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE schema_discussion.discussion_comment ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_discussion.discussion_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 254 (class 1259 OID 17118)
-- Name: discussion_comment_vote; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.discussion_comment_vote (
    user_id integer NOT NULL,
    comment_id integer NOT NULL
);


ALTER TABLE schema_discussion.discussion_comment_vote OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 17049)
-- Name: discussion_id_seq; Type: SEQUENCE; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE schema_discussion.discussion ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_discussion.discussion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 253 (class 1259 OID 17103)
-- Name: discussion_vote; Type: TABLE; Schema: schema_discussion; Owner: postgres; Tablespace: kodeholik_discussion_data
--

CREATE TABLE schema_discussion.discussion_vote (
    user_id integer NOT NULL,
    discussion_id integer NOT NULL
);


ALTER TABLE schema_discussion.discussion_vote OWNER TO postgres;

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
    acceptance_rate double precision,
    no_submission integer,
    status schema_problem.problem_status NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    updated_at timestamp without time zone,
    updated_by integer
);


ALTER TABLE schema_problem.problem OWNER TO postgres;

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
-- TOC entry 268 (class 1259 OID 17327)
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
    user_id integer,
    problem_id integer,
    code text NOT NULL,
    language_id integer,
    is_accepted bit(1) NOT NULL,
    notes character varying(200),
    execution_time double precision NOT NULL,
    memory_usage double precision NOT NULL,
    created_at timestamp without time zone NOT NULL,
    status schema_problem.submission_status NOT NULL
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
    main_function text NOT NULL
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
-- TOC entry 255 (class 1259 OID 17133)
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
    is_sample bit(1)
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
    level schema_setting.level NOT NULL,
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
-- TOC entry 262 (class 1259 OID 17244)
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
-- TOC entry 261 (class 1259 OID 17243)
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
    password character varying(20) NOT NULL,
    email character varying(200) NOT NULL,
    balance double precision NOT NULL,
    point integer NOT NULL,
    role schema_user.user_role NOT NULL,
    status schema_user.user_status NOT NULL,
    created_date date NOT NULL
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
-- TOC entry 5180 (class 0 OID 17264)
-- Dependencies: 264
-- Data for Name: contest; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5181 (class 0 OID 17281)
-- Dependencies: 265
-- Data for Name: contest_coworker; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5182 (class 0 OID 17296)
-- Dependencies: 266
-- Data for Name: contest_participant; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5183 (class 0 OID 17311)
-- Dependencies: 267
-- Data for Name: contest_problem_point; Type: TABLE DATA; Schema: schema_contest; Owner: postgres
--



--
-- TOC entry 5173 (class 0 OID 17154)
-- Dependencies: 257
-- Data for Name: chapter; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5164 (class 0 OID 17032)
-- Dependencies: 248
-- Data for Name: course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5175 (class 0 OID 17186)
-- Dependencies: 259
-- Data for Name: lesson; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5176 (class 0 OID 17208)
-- Dependencies: 260
-- Data for Name: lesson_problem; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- TOC entry 5166 (class 0 OID 17050)
-- Dependencies: 250
-- Data for Name: discussion; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--



--
-- TOC entry 5168 (class 0 OID 17081)
-- Dependencies: 252
-- Data for Name: discussion_comment; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--



--
-- TOC entry 5170 (class 0 OID 17118)
-- Dependencies: 254
-- Data for Name: discussion_comment_vote; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--



--
-- TOC entry 5169 (class 0 OID 17103)
-- Dependencies: 253
-- Data for Name: discussion_vote; Type: TABLE DATA; Schema: schema_discussion; Owner: postgres
--



--
-- TOC entry 5150 (class 0 OID 16730)
-- Dependencies: 234
-- Data for Name: problem; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--

INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (6, 'Bang', '1', 'EASY', 0, 0, 'PUBLIC', '2025-01-02 12:56:21.718047', 1, NULL, 1);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (13, 'Bang', '1', 'EASY', 0, 0, 'PUBLIC', '2025-01-02 13:29:35.752301', 1, NULL, NULL);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (2, 'Binh', '12323', 'EASY', 0, 0, 'PUBLIC', '2025-01-02 12:55:02.708558', 1, NULL, 1);
INSERT INTO schema_problem.problem (id, title, description, difficulty, acceptance_rate, no_submission, status, created_at, created_by, updated_at, updated_by) OVERRIDING SYSTEM VALUE VALUES (5, 'Binh', '12323', 'EASY', 0, 0, 'PUBLIC', '2025-01-02 12:56:17.748451', 1, NULL, 1);


--
-- TOC entry 5153 (class 0 OID 16777)
-- Dependencies: 237
-- Data for Name: problem_skill; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5155 (class 0 OID 16793)
-- Dependencies: 239
-- Data for Name: problem_solution; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5184 (class 0 OID 17327)
-- Dependencies: 268
-- Data for Name: problem_solution_skill; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5162 (class 0 OID 17004)
-- Dependencies: 246
-- Data for Name: problem_submission; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5158 (class 0 OID 16868)
-- Dependencies: 242
-- Data for Name: problem_template; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5152 (class 0 OID 16762)
-- Dependencies: 236
-- Data for Name: problem_topic; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5156 (class 0 OID 16840)
-- Dependencies: 240
-- Data for Name: solution_code; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5171 (class 0 OID 17133)
-- Dependencies: 255
-- Data for Name: solution_vote; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5160 (class 0 OID 16887)
-- Dependencies: 244
-- Data for Name: test_case; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5151 (class 0 OID 16747)
-- Dependencies: 235
-- Data for Name: user_favourite; Type: TABLE DATA; Schema: schema_problem; Owner: postgres
--



--
-- TOC entry 5146 (class 0 OID 16686)
-- Dependencies: 230
-- Data for Name: language; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--



--
-- TOC entry 5142 (class 0 OID 16650)
-- Dependencies: 226
-- Data for Name: skill; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--



--
-- TOC entry 5144 (class 0 OID 16668)
-- Dependencies: 228
-- Data for Name: topic; Type: TABLE DATA; Schema: schema_setting; Owner: postgres
--



--
-- TOC entry 5148 (class 0 OID 16704)
-- Dependencies: 232
-- Data for Name: notification; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--



--
-- TOC entry 5178 (class 0 OID 17244)
-- Dependencies: 262
-- Data for Name: transaction; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--



--
-- TOC entry 5140 (class 0 OID 16570)
-- Dependencies: 224
-- Data for Name: users; Type: TABLE DATA; Schema: schema_user; Owner: postgres
--

INSERT INTO schema_user.users (id, username, fullname, password, email, balance, point, role, status, created_date) OVERRIDING SYSTEM VALUE VALUES (1, 'mast', 'Tran Hai Bang', '1', 'tranhaibang665@gmail.com', 0, 0, 'STUDENT', 'ACTIVATED', '2024-03-20');


--
-- TOC entry 5190 (class 0 OID 0)
-- Dependencies: 263
-- Name: contest_id_seq; Type: SEQUENCE SET; Schema: schema_contest; Owner: postgres
--

SELECT pg_catalog.setval('schema_contest.contest_id_seq', 1, false);


--
-- TOC entry 5191 (class 0 OID 0)
-- Dependencies: 256
-- Name: chapter_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.chapter_id_seq', 1, false);


--
-- TOC entry 5192 (class 0 OID 0)
-- Dependencies: 247
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_id_seq', 1, false);


--
-- TOC entry 5193 (class 0 OID 0)
-- Dependencies: 258
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.lesson_id_seq', 1, false);


--
-- TOC entry 5194 (class 0 OID 0)
-- Dependencies: 251
-- Name: discussion_comment_id_seq; Type: SEQUENCE SET; Schema: schema_discussion; Owner: postgres
--

SELECT pg_catalog.setval('schema_discussion.discussion_comment_id_seq', 1, false);


--
-- TOC entry 5195 (class 0 OID 0)
-- Dependencies: 249
-- Name: discussion_id_seq; Type: SEQUENCE SET; Schema: schema_discussion; Owner: postgres
--

SELECT pg_catalog.setval('schema_discussion.discussion_id_seq', 1, false);


--
-- TOC entry 5196 (class 0 OID 0)
-- Dependencies: 233
-- Name: problem_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_id_seq', 23, true);


--
-- TOC entry 5197 (class 0 OID 0)
-- Dependencies: 245
-- Name: problem_submission_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problem_submission_id_seq', 1, false);


--
-- TOC entry 5198 (class 0 OID 0)
-- Dependencies: 238
-- Name: problemsolution_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problemsolution_id_seq', 1, false);


--
-- TOC entry 5199 (class 0 OID 0)
-- Dependencies: 241
-- Name: problemtemplate_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.problemtemplate_id_seq', 1, false);


--
-- TOC entry 5200 (class 0 OID 0)
-- Dependencies: 243
-- Name: testcase_id_seq; Type: SEQUENCE SET; Schema: schema_problem; Owner: postgres
--

SELECT pg_catalog.setval('schema_problem.testcase_id_seq', 1, false);


--
-- TOC entry 5201 (class 0 OID 0)
-- Dependencies: 229
-- Name: language_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.language_id_seq', 1, false);


--
-- TOC entry 5202 (class 0 OID 0)
-- Dependencies: 225
-- Name: skill_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.skill_id_seq', 1, false);


--
-- TOC entry 5203 (class 0 OID 0)
-- Dependencies: 227
-- Name: topic_id_seq; Type: SEQUENCE SET; Schema: schema_setting; Owner: postgres
--

SELECT pg_catalog.setval('schema_setting.topic_id_seq', 1, false);


--
-- TOC entry 5204 (class 0 OID 0)
-- Dependencies: 231
-- Name: notification_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.notification_id_seq', 1, false);


--
-- TOC entry 5205 (class 0 OID 0)
-- Dependencies: 261
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.transaction_id_seq', 1, false);


--
-- TOC entry 5206 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: schema_user; Owner: postgres
--

SELECT pg_catalog.setval('schema_user.users_id_seq', 1, true);


SET default_tablespace = '';

--
-- TOC entry 4929 (class 2606 OID 17285)
-- Name: contest_coworker contest_coworker_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_coworker
    ADD CONSTRAINT contest_coworker_pkey PRIMARY KEY (contest_id, user_id);


--
-- TOC entry 4931 (class 2606 OID 17300)
-- Name: contest_participant contest_participant_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_participant
    ADD CONSTRAINT contest_participant_pkey PRIMARY KEY (contest_id, user_id);


--
-- TOC entry 4927 (class 2606 OID 17270)
-- Name: contest contest_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest
    ADD CONSTRAINT contest_pkey PRIMARY KEY (id);


--
-- TOC entry 4933 (class 2606 OID 17315)
-- Name: contest_problem_point contest_problem_point_pkey; Type: CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_problem_point
    ADD CONSTRAINT contest_problem_point_pkey PRIMARY KEY (problem_id, contest_id);


--
-- TOC entry 4919 (class 2606 OID 17160)
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (id);


--
-- TOC entry 4907 (class 2606 OID 17038)
-- Name: course course_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- TOC entry 4921 (class 2606 OID 17192)
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- TOC entry 4923 (class 2606 OID 17212)
-- Name: lesson_problem lesson_problem_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_pkey PRIMARY KEY (lesson_id, problem_id);


--
-- TOC entry 4911 (class 2606 OID 17087)
-- Name: discussion_comment discussion_comment_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment
    ADD CONSTRAINT discussion_comment_pkey PRIMARY KEY (id);


--
-- TOC entry 4915 (class 2606 OID 17122)
-- Name: discussion_comment_vote discussion_comment_vote_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment_vote
    ADD CONSTRAINT discussion_comment_vote_pkey PRIMARY KEY (user_id, comment_id);


--
-- TOC entry 4909 (class 2606 OID 17056)
-- Name: discussion discussion_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion
    ADD CONSTRAINT discussion_pkey PRIMARY KEY (id);


--
-- TOC entry 4913 (class 2606 OID 17107)
-- Name: discussion_vote discussion_vote_pkey; Type: CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_vote
    ADD CONSTRAINT discussion_vote_pkey PRIMARY KEY (user_id, discussion_id);


--
-- TOC entry 4889 (class 2606 OID 17397)
-- Name: problem problem_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_pkey PRIMARY KEY (id);


--
-- TOC entry 4935 (class 2606 OID 17331)
-- Name: problem_solution_skill problem_solution_skill_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_pkey PRIMARY KEY (problem_solution_id, skill_id);


--
-- TOC entry 4905 (class 2606 OID 17010)
-- Name: problem_submission problem_submission_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_pkey PRIMARY KEY (id);


--
-- TOC entry 4895 (class 2606 OID 16781)
-- Name: problem_skill problemskill_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_pkey PRIMARY KEY (problem_id, skill_id);


--
-- TOC entry 4897 (class 2606 OID 16799)
-- Name: problem_solution problemsolution_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT problemsolution_pkey PRIMARY KEY (id);


--
-- TOC entry 4901 (class 2606 OID 16874)
-- Name: problem_template problemtemplate_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_pkey PRIMARY KEY (id);


--
-- TOC entry 4893 (class 2606 OID 16766)
-- Name: problem_topic problemtopic_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_pkey PRIMARY KEY (problem_id, topic_id);


--
-- TOC entry 4917 (class 2606 OID 17137)
-- Name: solution_vote solution_vote_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_pkey PRIMARY KEY (user_id, solution_id);


--
-- TOC entry 4899 (class 2606 OID 16846)
-- Name: solution_code solutioncode_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_pkey PRIMARY KEY (solution_id);


--
-- TOC entry 4903 (class 2606 OID 16893)
-- Name: test_case testcase_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT testcase_pkey PRIMARY KEY (id);


--
-- TOC entry 4891 (class 2606 OID 16751)
-- Name: user_favourite userfavourite_pkey; Type: CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_pkey PRIMARY KEY (user_id, problem_id);


--
-- TOC entry 4883 (class 2606 OID 16692)
-- Name: language language_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_name_key UNIQUE (name);


--
-- TOC entry 4885 (class 2606 OID 16690)
-- Name: language language_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- TOC entry 4875 (class 2606 OID 16656)
-- Name: skill skill_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_name_key UNIQUE (name);


--
-- TOC entry 4877 (class 2606 OID 16654)
-- Name: skill skill_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_pkey PRIMARY KEY (id);


--
-- TOC entry 4879 (class 2606 OID 16674)
-- Name: topic topic_name_key; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_name_key UNIQUE (name);


--
-- TOC entry 4881 (class 2606 OID 16672)
-- Name: topic topic_pkey; Type: CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- TOC entry 4887 (class 2606 OID 16710)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 4925 (class 2606 OID 17250)
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);


--
-- TOC entry 4869 (class 2606 OID 16578)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4871 (class 2606 OID 16574)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4873 (class 2606 OID 16576)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4986 (class 2606 OID 17286)
-- Name: contest_coworker contest_coworker_contest_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_coworker
    ADD CONSTRAINT contest_coworker_contest_id_fkey FOREIGN KEY (contest_id) REFERENCES schema_contest.contest(id);


--
-- TOC entry 4987 (class 2606 OID 17291)
-- Name: contest_coworker contest_coworker_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_coworker
    ADD CONSTRAINT contest_coworker_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4984 (class 2606 OID 17271)
-- Name: contest contest_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest
    ADD CONSTRAINT contest_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4988 (class 2606 OID 17301)
-- Name: contest_participant contest_participant_contest_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_participant
    ADD CONSTRAINT contest_participant_contest_id_fkey FOREIGN KEY (contest_id) REFERENCES schema_contest.contest(id);


--
-- TOC entry 4989 (class 2606 OID 17306)
-- Name: contest_participant contest_participant_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_participant
    ADD CONSTRAINT contest_participant_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4990 (class 2606 OID 17321)
-- Name: contest_problem_point contest_problem_point_contest_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_problem_point
    ADD CONSTRAINT contest_problem_point_contest_id_fkey FOREIGN KEY (contest_id) REFERENCES schema_contest.contest(id);


--
-- TOC entry 4991 (class 2606 OID 17443)
-- Name: contest_problem_point contest_problem_point_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest_problem_point
    ADD CONSTRAINT contest_problem_point_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4985 (class 2606 OID 17276)
-- Name: contest contest_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_contest; Owner: postgres
--

ALTER TABLE ONLY schema_contest.contest
    ADD CONSTRAINT contest_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4975 (class 2606 OID 17161)
-- Name: chapter chapter_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 4976 (class 2606 OID 17166)
-- Name: chapter chapter_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4977 (class 2606 OID 17171)
-- Name: chapter chapter_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4961 (class 2606 OID 17039)
-- Name: course course_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4962 (class 2606 OID 17044)
-- Name: course course_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4978 (class 2606 OID 17193)
-- Name: lesson lesson_chapter_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES schema_course.chapter(id);


--
-- TOC entry 4979 (class 2606 OID 17198)
-- Name: lesson lesson_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4981 (class 2606 OID 17213)
-- Name: lesson_problem lesson_problem_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id);


--
-- TOC entry 4982 (class 2606 OID 17438)
-- Name: lesson_problem lesson_problem_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4980 (class 2606 OID 17203)
-- Name: lesson lesson_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4966 (class 2606 OID 17093)
-- Name: discussion_comment discussion_comment_comment_reply_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment
    ADD CONSTRAINT discussion_comment_comment_reply_id_fkey FOREIGN KEY (comment_reply_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4967 (class 2606 OID 17098)
-- Name: discussion_comment discussion_comment_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment
    ADD CONSTRAINT discussion_comment_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4968 (class 2606 OID 17088)
-- Name: discussion_comment discussion_comment_discussion_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment
    ADD CONSTRAINT discussion_comment_discussion_id_fkey FOREIGN KEY (discussion_id) REFERENCES schema_discussion.discussion(id);


--
-- TOC entry 4971 (class 2606 OID 17128)
-- Name: discussion_comment_vote discussion_comment_vote_comment_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment_vote
    ADD CONSTRAINT discussion_comment_vote_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES schema_discussion.discussion_comment(id);


--
-- TOC entry 4972 (class 2606 OID 17123)
-- Name: discussion_comment_vote discussion_comment_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_comment_vote
    ADD CONSTRAINT discussion_comment_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4963 (class 2606 OID 17057)
-- Name: discussion discussion_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion
    ADD CONSTRAINT discussion_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- TOC entry 4964 (class 2606 OID 17062)
-- Name: discussion discussion_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion
    ADD CONSTRAINT discussion_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4965 (class 2606 OID 17067)
-- Name: discussion discussion_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion
    ADD CONSTRAINT discussion_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4969 (class 2606 OID 17113)
-- Name: discussion_vote discussion_vote_discussion_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_vote
    ADD CONSTRAINT discussion_vote_discussion_id_fkey FOREIGN KEY (discussion_id) REFERENCES schema_discussion.discussion(id);


--
-- TOC entry 4970 (class 2606 OID 17108)
-- Name: discussion_vote discussion_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_discussion; Owner: postgres
--

ALTER TABLE ONLY schema_discussion.discussion_vote
    ADD CONSTRAINT discussion_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4943 (class 2606 OID 16737)
-- Name: problem problem_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4992 (class 2606 OID 17332)
-- Name: problem_solution_skill problem_solution_skill_problem_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_problem_solution_id_fkey FOREIGN KEY (problem_solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4993 (class 2606 OID 17337)
-- Name: problem_solution_skill problem_solution_skill_skill_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution_skill
    ADD CONSTRAINT problem_solution_skill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES schema_setting.skill(id);


--
-- TOC entry 4958 (class 2606 OID 17021)
-- Name: problem_submission problem_submission_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 4959 (class 2606 OID 17433)
-- Name: problem_submission problem_submission_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4960 (class 2606 OID 17011)
-- Name: problem_submission problem_submission_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_submission
    ADD CONSTRAINT problem_submission_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4944 (class 2606 OID 16742)
-- Name: problem problem_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem
    ADD CONSTRAINT problem_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4949 (class 2606 OID 17408)
-- Name: problem_skill problemskill_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4950 (class 2606 OID 16787)
-- Name: problem_skill problemskill_skill_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_skill
    ADD CONSTRAINT problemskill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES schema_setting.skill(id);


--
-- TOC entry 4951 (class 2606 OID 17413)
-- Name: problem_solution problemsolution_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_solution
    ADD CONSTRAINT problemsolution_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4955 (class 2606 OID 16880)
-- Name: problem_template problemtemplate_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 4956 (class 2606 OID 17423)
-- Name: problem_template problemtemplate_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_template
    ADD CONSTRAINT problemtemplate_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4947 (class 2606 OID 17403)
-- Name: problem_topic problemtopic_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4948 (class 2606 OID 16772)
-- Name: problem_topic problemtopic_topic_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.problem_topic
    ADD CONSTRAINT problemtopic_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES schema_setting.topic(id);


--
-- TOC entry 4973 (class 2606 OID 17143)
-- Name: solution_vote solution_vote_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_solution_id_fkey FOREIGN KEY (solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4974 (class 2606 OID 17138)
-- Name: solution_vote solution_vote_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_vote
    ADD CONSTRAINT solution_vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4952 (class 2606 OID 16857)
-- Name: solution_code solutioncode_language_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_language_id_fkey FOREIGN KEY (language_id) REFERENCES schema_setting.language(id);


--
-- TOC entry 4953 (class 2606 OID 17418)
-- Name: solution_code solutioncode_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4954 (class 2606 OID 16847)
-- Name: solution_code solutioncode_solution_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.solution_code
    ADD CONSTRAINT solutioncode_solution_id_fkey FOREIGN KEY (solution_id) REFERENCES schema_problem.problem_solution(id);


--
-- TOC entry 4957 (class 2606 OID 17428)
-- Name: test_case testcase_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.test_case
    ADD CONSTRAINT testcase_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4945 (class 2606 OID 17398)
-- Name: user_favourite userfavourite_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- TOC entry 4946 (class 2606 OID 16752)
-- Name: user_favourite userfavourite_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_problem; Owner: postgres
--

ALTER TABLE ONLY schema_problem.user_favourite
    ADD CONSTRAINT userfavourite_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4940 (class 2606 OID 16693)
-- Name: language language_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4941 (class 2606 OID 16698)
-- Name: language language_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.language
    ADD CONSTRAINT language_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4936 (class 2606 OID 16657)
-- Name: skill skill_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4937 (class 2606 OID 16662)
-- Name: skill skill_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.skill
    ADD CONSTRAINT skill_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4938 (class 2606 OID 16675)
-- Name: topic topic_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4939 (class 2606 OID 16680)
-- Name: topic topic_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_setting; Owner: postgres
--

ALTER TABLE ONLY schema_setting.topic
    ADD CONSTRAINT topic_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- TOC entry 4942 (class 2606 OID 16711)
-- Name: notification notification_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


--
-- TOC entry 4983 (class 2606 OID 17251)
-- Name: transaction transaction_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_user; Owner: postgres
--

ALTER TABLE ONLY schema_user.transaction
    ADD CONSTRAINT transaction_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id);


-- Completed on 2025-01-17 19:07:54

--
-- PostgreSQL database dump complete
--

