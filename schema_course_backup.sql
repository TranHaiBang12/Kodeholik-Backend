--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

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
-- Name: schema_course; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA schema_course;


ALTER SCHEMA schema_course OWNER TO postgres;

--
-- Name: chapter_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.chapter_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.chapter_status OWNER TO postgres;

--
-- Name: course_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.course_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED'
);


ALTER TYPE schema_course.course_status OWNER TO postgres;

--
-- Name: lesson_status; Type: TYPE; Schema: schema_course; Owner: postgres
--

CREATE TYPE schema_course.lesson_status AS ENUM (
    'ACTIVATED',
    'INACTIVATED',
    'IN_PROGRESS'
);


ALTER TYPE schema_course.lesson_status OWNER TO postgres;

--
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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: chapter; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.chapter (
    id bigint NOT NULL,
    course_id bigint,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    display_order integer,
    status character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    updated_at timestamp without time zone,
    updated_by bigint
);


ALTER TABLE schema_course.chapter OWNER TO postgres;

--
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
-- Name: course; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    description text NOT NULL,
    image character varying(255),
    status character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    updated_at timestamp without time zone,
    updated_by bigint,
    rate double precision,
    number_of_participant integer DEFAULT 0
);


ALTER TABLE schema_course.course OWNER TO postgres;

--
-- Name: course_comment; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_comment (
    course_id bigint NOT NULL,
    comment_id bigint NOT NULL,
    no_upvote integer,
    created_at timestamp without time zone,
    created_by integer,
    updated_at timestamp without time zone,
    updated_by integer,
    no_comment integer
);


ALTER TABLE schema_course.course_comment OWNER TO postgres;

--
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
-- Name: course_rating; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_rating (
    id bigint NOT NULL,
    course_id bigint NOT NULL,
    user_id bigint NOT NULL,
    rating integer,
    comment text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT course_rating_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


ALTER TABLE schema_course.course_rating OWNER TO postgres;

--
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
-- Name: course_topic; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_topic (
    course_id bigint NOT NULL,
    topic_id bigint NOT NULL
);


ALTER TABLE schema_course.course_topic OWNER TO postgres;

--
-- Name: course_user; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.course_user (
    course_id bigint NOT NULL,
    user_id bigint NOT NULL,
    enrolled_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    study_streak integer,
    last_studied_start timestamp without time zone,
    last_studied_end timestamp without time zone,
    study_time bigint,
    finished boolean
);


ALTER TABLE schema_course.course_user OWNER TO postgres;

--
-- Name: lesson; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.lesson (
    id bigint NOT NULL,
    chapter_id bigint,
    title character varying(100) NOT NULL,
    description text NOT NULL,
    display_order integer,
    type character varying(255) NOT NULL,
    video_url character varying(200),
    attached_file character varying(250),
    created_at timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    updated_at timestamp without time zone,
    updated_by bigint,
    status character varying(255)
);


ALTER TABLE schema_course.lesson OWNER TO postgres;

--
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
-- Name: lesson_problem; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.lesson_problem (
    lesson_id bigint NOT NULL,
    problem_id bigint NOT NULL
);


ALTER TABLE schema_course.lesson_problem OWNER TO postgres;

--
-- Name: top_course; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.top_course (
    id bigint NOT NULL,
    course_id bigint,
    display_order integer
);


ALTER TABLE schema_course.top_course OWNER TO postgres;

--
-- Name: top_course_id_seq; Type: SEQUENCE; Schema: schema_course; Owner: postgres
--

ALTER TABLE schema_course.top_course ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME schema_course.top_course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_lesson_progress; Type: TABLE; Schema: schema_course; Owner: postgres
--

CREATE TABLE schema_course.user_lesson_progress (
    user_id bigint NOT NULL,
    lesson_id bigint NOT NULL
);


ALTER TABLE schema_course.user_lesson_progress OWNER TO postgres;

--
-- Data for Name: chapter; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (1, 1, 'Chương 1: Giới thiệu khóa học', 'Nội dung giới thiệu tổng quan về khóa học', 1, 'ACTIVATED', '2023-10-27 10:00:00', 1, '2023-10-27 11:00:00', 1);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (3, 2, 'Chương 2: Các khái niệm cơ bản', 'Các khái niệm cơ bản', 1, 'ACTIVATED', '2023-10-27 10:00:00', 1, '2023-10-27 11:00:00', 1);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (4, 1, 'Chương 2: Các khái niệm cơ bản', 'Các khái niệm cơ bản', 1, 'ACTIVATED', '2023-10-27 10:00:00', 1, '2023-10-27 11:00:00', 1);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (5, 1, 'thunghiem', 'A complete guide to learn Spring Boot', 1, 'ACTIVATED', '2025-02-28 17:25:29.131767', 1, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (6, 47, 'Chapter 1: List Data Structure', 'Describe the list data structure and its’ different way of implementations. Implement the singly linked list.

1.1. Describe list structures

1.2. Describe self-referential structures

1.3. Explain types of linked lists

1.4. Singly Linked Lists

1.5. Circular Lists

1.6. Doubly Linked Lists

1.7. Lists in java.util
', 1, 'ACTIVATED', '2025-04-08 16:37:44.695782', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (7, 47, 'Chapter 2: Stack and Queue', 'Define stack and queue. Describe basic operations and the use of these structures.
', 2, 'ACTIVATED', '2025-04-08 16:43:31.903179', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (8, 49, 'Chapter 3: Trees', 'Describe the list data structure and its’ different way of implementations. Implement the singly linked list. LO1.1 Demonstrate the list data structure with basic operations: insert, remove, edit, search, sort,... LO1.2 Explain two ways of implementing a list: using arrays and using linked lists. LO1.3 Write programs to implement the singly linked list with basic operations: add (last, first, before, after, at position), search, edit, remove, reverse, sort... ) LO1.4 Understand the doubly linked-list data structure with basic operations. LO1.5 Write code snippets which manipulate operations on doubly linked list, and use diagrams to illustrate the effect. LO1.6 Describe the circularly linked-list data structure with some basic operations like add, remove. LO1.7 Expain why the Java code library provides the ArrayList and LinkedList classes', 0, 'ACTIVATED', '2025-04-09 03:43:43.421325', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (10, 47, 'Chapter 4: Trees', '
![trees.png](s3:kodeholik-problem-image-1678ef4b-e973-4133-9c4a-06990a25fe95)

Explain about general tree, Binary Tree and Binary Search Tree (BST). Implement BST with basic operations. LO4.1 Define general tree, Binary Tree and Binary Search Tree (BST). LO4.2 Given a BST and a sequence of insert and delete operations on it, draw the resulted tree. LO4.3 Find the smallest and largest elements, number of nodes in a tree and its’ height. LO4.4 Write code to implement features of a binary search tree, such as insertion, deletion, searching, traversals, nodes and height calculation, rotation ... LO4.5 Derive the time complexities for the above operations on a binary search tree. LO4.6 Compare a binary search tree over other data structures that we have discussed in class. LO4.7 Identify applications where a binary search tree will be useful. LO4.8 Define balanced BST and explane simple balancing algorithm. LO4.9 Define AVL Tree and explain by examples the insertion and deletion operations in it. LO5.10 Define heap and explain its’ application.', 4, 'ACTIVATED', '2025-04-09 03:56:23.127623', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (11, 49, 'Chapter 1: Introduction', '                          ', 1, 'ACTIVATED', '2025-04-09 03:57:50.418663', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (12, 49, 'Chapter 3: Recursion', '            ', 3, 'ACTIVATED', '2025-04-09 03:58:54.228009', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (9, 47, 'Chapter 3: Recursions', '
![Types-of-Recursion.png](s3:kodeholik-problem-image-023645b6-f349-4789-8a09-df8e5406860c)
', 3, 'INACTIVATED', '2025-04-09 03:50:43.471528', 22, '2025-04-09 04:40:42.294978', 22);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (13, 49, 'Chapter 2: Stack & Queue', 'Chapter 2: Stack & Queue', 2, 'ACTIVATED', '2025-04-10 02:49:19.390991', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (14, 50, 'Chapter 1: Variables, Constants, and Data Types', 'This chapter introduces variables, constants, and data types in C. Students will learn how to declare and initialize variables, understand memory operations, and use basic input/output functions.', 1, 'ACTIVATED', '2025-04-15 16:06:45.534672', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (15, 50, 'Chapter 2: Expressions and Operators', 'This chapter covers different types of expressions and operators in C, including arithmetic, logical, and bitwise operations.', 2, 'ACTIVATED', '2025-04-15 16:07:12.588226', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (16, 50, 'Chapter 3: Control Structures', 'Learn how to control the flow of a program using selection and loop structures.', 3, 'ACTIVATED', '2025-04-15 16:07:42.02631', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (17, 50, 'Chapter 4: Functions and Modular Programming', 'Understand how to write modular programs using functions in C.', 4, 'ACTIVATED', '2025-04-15 16:09:07.446596', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (18, 50, 'Chapter 5: Pointers in C', 'Learn how pointers work and how to use them for efficient memory manipulation.', 5, 'ACTIVATED', '2025-04-15 16:09:26.888708', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (19, 50, 'Chapter 6: Arrays and Structures', 'Work with arrays and structures to store and manage data effectively.', 6, 'ACTIVATED', '2025-04-15 16:09:49.649927', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (20, 50, 'Chapter 7: String Handling in C', 'Understand how to work with strings and perform string operations.', 7, 'ACTIVATED', '2025-04-15 20:42:41.122484', 22, NULL, NULL);


--
-- Data for Name: course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (5, 'Introduction to Java', 'A complete guide to learn Spring Boot', 'https://example.com/spring-boot-course.jpg', 'ACTIVATED', '2025-02-26 19:43:53.237', 1, '2025-02-26 21:10:09.312', 1, 0, 678);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (6, 'Java Spring Boot', '', 'https://example.com/spring-boot-course.jpg', 'ACTIVATED', '2025-02-26 21:24:42.695', 1, '2025-02-26 21:24:42.695', 1, 0, 211);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (8, 'Spring Boot Advanced ', 'Updated description', NULL, 'ACTIVATED', '2025-02-28 20:57:41.903', 1, '2025-02-28 20:57:41.903', 1, 0, 1231);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (9, 'Java Algorithm ', 'Updated description', 'kodeholik-course-image-8969206e-b226-4264-afc0-55d709e3398f', 'ACTIVATED', '2025-02-28 21:01:05.145', 1, '2025-02-28 21:01:05.145', 1, 2, 456);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (4, 'Data Science', 'Analyze data', 'data.gif', 'ACTIVATED', '2025-02-17 09:00:00', 3, '2025-02-17 10:00:00', 3, 1, 232);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (7, 'Spring Security Tutorial', 'Updated description', 'https://kodeholik.s3.ap-southeast-1.amazonaws.com/courses/8ad2b872-6d25-4727-ac0a-c5e2e0caf326-temp6.jpg', 'ACTIVATED', '2025-02-28 15:58:43.504', 1, '2025-02-28 15:58:43.504', 1, 3.5, 111);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (10, 'Docker Tutorial', 'aádfasdfasdfasd', NULL, 'ACTIVATED', '2025-03-13 21:07:10.076', 1, '2025-03-13 21:07:10.076', 1, 0, 231);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (3, 'Web Development', 'Build websites', 'kodeholik-course-image-8969206e-b226-4264-afc0-55d709e3398f', 'INACTIVATED', '2025-02-16 14:30:00', 2, '2025-02-16 15:30:00', 2, 3, 500);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (2, 'Introduction to Python', 'Learn the basics of Python', 'python.jpg', 'ACTIVATED', '2025-02-15 10:00:00', 1, '2025-02-15 11:00:00', 1, 5, 666);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (11, 'Java Basic For Beginer1', 'Learn Java Basic', NULL, 'ACTIVATED', '2025-03-31 13:38:21.312', 22, '2025-03-31 13:38:21.312', 22, 0, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (1, 'Java Data Structure', 'A complete guide to learn Spring Boot', 'https://example.com/spring-boot-course.jpg', 'ACTIVATED', '2025-02-14 23:04:27.938712', 70, '2025-02-26 20:58:09.965', 1, 2, 1001);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (44, 'Object-Oriented Programming Demo', '
![OOP.png](s3:kodeholik-problem-image-4390b4fb-3900-4673-b616-34a3eee3af99)
Welcome to the journey of discovering the power of object-oriented programming in Java!

In today''s technical world, understanding and mastering object-oriented programming is the key to unlocking creativity and productivity for any programmer. Java, with its clear and powerful syntax, is not only one of the most popular programming languages, but also a powerful platform for developing applications with the object-oriented programming model.

In this course, we will dive into the world of object-oriented programming and explore how Java applies it in practice. You will start with basic concepts such as classes, objects, and inheritance, and then move on to more complex concepts such as overriding, polymorphism, and packages.

The course is not only about imparting knowledge but also about building practical skills. By combining theory with exercises and real-world projects, you will have the opportunity to develop your programming skills and gain a better understanding of how to apply object-oriented programming in real-world projects.

Object-oriented programming (OOP) in Java is a powerful and flexible programming approach that allows you to organize your source code into meaningful objects, making it easier to manage and reuse source code. Here are some common applications of object-oriented programming in Java:

Inheritance: Inheritance allows a child class to inherit all the properties and methods of its parent class. This helps reuse source code and create new classes based on existing classes.

Encapsulation: Encapsulation helps hide the information and settings of an object from other objects, allowing access only through public methods. This helps improve security and maintainability of source code.

Polymorphism: Polymorphism allows objects of subclasses to be treated as objects of the parent class, providing flexibility in handling objects and implementing methods in a polymorphic manner.

Packages: Java uses packages to organize and manage related classes and packages. This helps create a clear and easily maintainable project structure.

Abstract Classes and Interfaces: Abstract classes and interfaces allow you to define contracts for subclasses. Abstract classes provide an implementation, while interfaces specify the methods that the classes must implement.

Reusability: OOP in Java helps increase code reuse. You can reuse the classes that have been built to create new applications without having to rewrite the code from scratch.

State Management: For large and complex applications, managing the state of objects is very important. OOP in Java helps to organize and manage the state of objects easily.', 'kodeholik-course-image-aadf98da-bad7-43e7-be8d-be032e985a9c', 'ACTIVATED', '2025-03-31 13:45:36.744', 22, '2025-04-08 13:35:22.669', 22, 0, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (46, 'Object-Oriented ', 'Welcome to the journey of discovering the power of object-oriented programming in Java!

In today''s technical world, understanding and mastering object-oriented programming is the key to unlocking creativity and productivity for any programmer. Java, with its clear and powerful syntax, is not only one of the most popular programming languages, but also a powerful platform for developing applications with the object-oriented programming model.

In this course, we will dive into the world of object-oriented programming and explore how Java applies it in practice. You will start with basic concepts such as classes, objects, and inheritance, and then move on to more complex concepts such as overriding, polymorphism, and packages.

The course is not only about imparting knowledge but also about building practical skills. By combining theory with exercises and real-world projects, you will have the opportunity to develop your programming skills and gain a better understanding of how to apply object-oriented programming in real-world projects.

Object-oriented programming (OOP) in Java is a powerful and flexible programming approach that allows you to organize your source code into meaningful objects, making it easier to manage and reuse source code. Here are some common applications of object-oriented programming in Java:

Inheritance: Inheritance allows a child class to inherit all the properties and methods of its parent class. This helps reuse source code and create new classes based on existing classes.

Encapsulation: Encapsulation helps hide the information and settings of an object from other objects, allowing access only through public methods. This helps improve security and maintainability of source code.

Polymorphism: Polymorphism allows objects of subclasses to be treated as objects of the parent class, providing flexibility in handling objects and implementing methods in a polymorphic manner.

Packages: Java uses packages to organize and manage related classes and packages. This helps create a clear and easily maintainable project structure.

Abstract Classes and Interfaces: Abstract classes and interfaces allow you to define contracts for subclasses. Abstract classes provide an implementation, while interfaces specify the methods that the classes must implement.

Reusability: OOP in Java helps increase code reuse. You can reuse the classes that have been built to create new applications without having to rewrite the code from scratch.

State Management: For large and complex applications, managing the state of objects is very important. OOP in Java helps to organize and manage the state of objects easily.
Welcome to the journey of discovering the power of object-oriented programming in Java!', NULL, 'INACTIVATED', '2025-04-08 13:38:16.537', 22, '2025-04-08 13:39:02.326', 22, 0, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (45, 'Object Oriented Programming', '
![OOP.png](s3:kodeholik-problem-image-e578b586-1be0-4fd1-80fe-ad0dde2c1767)

Welcome to the journey of discovering the power of object-oriented programming in Java!

In today''s technical world, understanding and mastering object-oriented programming is the key to unlocking creativity and productivity for any programmer. Java, with its clear and powerful syntax, is not only one of the most popular programming languages, but also a powerful platform for developing applications with the object-oriented programming model.

In this course, we will dive into the world of object-oriented programming and explore how Java applies it in practice. You will start with basic concepts such as classes, objects, and inheritance, and then move on to more complex concepts such as overriding, polymorphism, and packages.

The course is not only about imparting knowledge but also about building practical skills. By combining theory with exercises and real-world projects, you will have the opportunity to develop your programming skills and gain a better understanding of how to apply object-oriented programming in real-world projects.

Object-oriented programming (OOP) in Java is a powerful and flexible programming approach that allows you to organize your source code into meaningful objects, making it easier to manage and reuse source code. Here are some common applications of object-oriented programming in Java:

Inheritance: Inheritance allows a child class to inherit all the properties and methods of its parent class. This helps reuse source code and create new classes based on existing classes.

Encapsulation: Encapsulation helps hide the information and settings of an object from other objects, allowing access only through public methods. This helps improve security and maintainability of source code.

Polymorphism: Polymorphism allows objects of subclasses to be treated as objects of the parent class, providing flexibility in handling objects and implementing methods in a polymorphic manner.

Packages: Java uses packages to organize and manage related classes and packages. This helps create a clear and easily maintainable project structure.

Abstract Classes and Interfaces: Abstract classes and interfaces allow you to define contracts for subclasses. Abstract classes provide an implementation, while interfaces specify the methods that the classes must implement.

Reusability: OOP in Java helps increase code reuse. You can reuse the classes that have been built to create new applications without having to rewrite the code from scratch.

State Management: For large and complex applications, managing the state of object is very important. OOP in Java helps to organize and manage the state of objects easily.', 'kodeholik-course-image-33b79361-3946-454a-bfe0-4136bfdcba1c', 'ACTIVATED', '2025-04-08 13:14:38.637', 22, '2025-04-08 16:22:01.766', 22, 0, 1);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (47, 'Data Structures and Algorithm', '
![CSD.png](s3:kodeholik-problem-image-15ce8bea-b816-497c-9c95-4738a00b8903)

Upon finishing the course, students can:
1) Knowledge: Understand 
- the connection between data structures and their algorithms, including an analysis of algorithms'' complexity;
- data structurre in the context of object-oriented program design;
- how data structure are implemented in an OO programming language such as Java
2) Able to 
- organize and manipulate basic structures: array, linked list, tree, heap, hash
- use algorithms for traversing, sorting, searching on studying structures
- select a suitable algorithm to solve a practical problem
3) Able to 
- use JAVA programming language for solving some problems
- use Eclipse tool for developing programs in JAVA
- Implement some programs in JAVA to solve practical problems based on the studying algorithms
4) Others: 
- Improve study skills (academic reading, information searching, ...)

', 'kodeholik-course-image-ccd38d8e-0e50-4cdc-926e-bd3f78d67f77', 'ACTIVATED', '2025-04-08 13:55:17.792', 22, '2025-04-08 15:05:33.8', 22, 0, 1);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (48, 'Data Structures & Algorithm', '
![CSD.png](s3:kodeholik-problem-image-5577e1d2-b8f7-448f-ac78-9cd52759ab83)

Upon finishing the course, students can:
1) Knowledge: Understand 
- the connection between data structures and their algorithms, including an analysis of algorithms'' complexity;
- data structurre in the context of object-oriented program design;
- how data structure are implemented in an OO programming language such as Java
2) Able to 
- organize and manipulate basic structures: array, linked list, tree, heap, hash
- use algorithms for traversing, sorting, searching on studying structures
- select a suitable algorithm to solve a practical problem
3) Able to 
- use JAVA programming language for solving some problems
- use Eclipse tool for developing programs in JAVA
- Implement some programs in JAVA to solve practical problems based on the studying algorithms
4) Others: 
- Improve study skills (academic reading, information searching, ...)

', 'kodeholik-course-image-f010ac47-d433-4dda-86d6-97a0f6a4e4d2', 'INACTIVATED', '2025-04-08 13:57:18.26', 22, '2025-04-08 13:57:18.26', 22, 0, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (49, 'Data Structures & Algorithm', '
https://codelearn.io/learning/data-structure-and-algorithms?tab=introduce

Upon finishing the course, students can:
1) Knowledge: Understand 
- the connection between data structures and their algorithms, including an analysis of algorithms'' complexity;
- data structurre in the context of object-oriented program design;
- how data structure are implemented in an OO programming language such as Java
2) Able to 
- organize and manipulate basic structures: array, linked list, tree, heap, hash
- use algorithms for traversing, sorting, searching on studying structures
- select a suitable algorithm to solve a practical problem
3) Able to 
- use JAVA programming language for solving some problems
- use Eclipse tool for developing programs in JAVA
- Implement some programs in JAVA to solve practical problems based on the studying algorithms
4) Others: 
- Improve study skills (academic reading, information searching, ...)

', 'kodeholik-course-image-80fdc2b1-791a-4c75-a72a-594ec04f7c6e', 'INACTIVATED', '2025-04-08 14:02:27.997', 22, '2025-04-08 14:02:27.997', 22, 0, 0);
INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (50, 'PRF192 - Programming Fundamentals', '## Course Description

This course provides fundamental knowledge of computer systems and software development methods, focusing on function-oriented programming design, coding, testing, and programming discipline. Students will learn essential programming concepts, modularity, and coding in C language.

## Learning Outcomes

### **1. Knowledge (ABET e)**

- Explain how to solve real-world problems using computers.
- Understand basic concepts of computer systems and software development.
- Grasp fundamental programming concepts with a focus on procedural programming, testing, debugging, and unit testing.

### **2. Programming Skills (ABET k)**

- Read and comprehend simple C programs.
- Solve real-world problems using the C programming language.

### **3. Effective Learning Methods (ABET i)**

- Develop academic reading skills.
- Enhance individual and teamwork behaviors.', 'kodeholik-course-image-9fac97ed-234c-421b-8a8a-602c36c214c2', 'ACTIVATED', '2025-04-15 16:05:55.422', 22, '2025-04-15 16:05:55.422', 22, 0, 1);


--
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
-- Data for Name: course_rating; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (1, 3, 1, 2, 'This course is amazing! Do not recommended.', '2025-02-25 17:31:31.986', '2025-03-13 13:48:35.581');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (3, 9, 1, 2, 'This course is amazing! Do not recommended.', '2025-03-13 17:02:45.266', '2025-03-13 17:02:45.266');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (4, 4, 1, 1, 'This course is amazing! Do not recommended.', '2025-03-13 17:10:14.969', '2025-03-13 17:10:14.969');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (5, 2, 1, 5, 'This course is amazing! Do not recommended.', '2025-03-13 17:10:36.624', '2025-03-13 17:10:36.624');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (2, 7, 2, 2, 'This course is amazing! Do not recommended.', '2025-03-13 17:02:29.341', '2025-03-13 17:02:29.341');
INSERT INTO schema_course.course_rating OVERRIDING SYSTEM VALUE VALUES (6, 7, 1, 5, 'This course is amazing! Do not recommended.', '2025-03-13 17:11:13.157', '2025-03-13 17:11:13.157');


--
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
INSERT INTO schema_course.course_topic VALUES (44, 61);
INSERT INTO schema_course.course_topic VALUES (44, 28);
INSERT INTO schema_course.course_topic VALUES (44, 62);
INSERT INTO schema_course.course_topic VALUES (44, 8);
INSERT INTO schema_course.course_topic VALUES (46, 1);
INSERT INTO schema_course.course_topic VALUES (47, 27);
INSERT INTO schema_course.course_topic VALUES (47, 18);
INSERT INTO schema_course.course_topic VALUES (47, 7);
INSERT INTO schema_course.course_topic VALUES (47, 4);
INSERT INTO schema_course.course_topic VALUES (47, 25);
INSERT INTO schema_course.course_topic VALUES (47, 2);
INSERT INTO schema_course.course_topic VALUES (47, 5);
INSERT INTO schema_course.course_topic VALUES (47, 6);
INSERT INTO schema_course.course_topic VALUES (47, 26);
INSERT INTO schema_course.course_topic VALUES (47, 3);
INSERT INTO schema_course.course_topic VALUES (47, 8);
INSERT INTO schema_course.course_topic VALUES (45, 63);
INSERT INTO schema_course.course_topic VALUES (45, 64);
INSERT INTO schema_course.course_topic VALUES (45, 61);
INSERT INTO schema_course.course_topic VALUES (45, 66);
INSERT INTO schema_course.course_topic VALUES (45, 65);
INSERT INTO schema_course.course_topic VALUES (45, 28);
INSERT INTO schema_course.course_topic VALUES (45, 2);
INSERT INTO schema_course.course_topic VALUES (45, 62);
INSERT INTO schema_course.course_topic VALUES (45, 6);
INSERT INTO schema_course.course_topic VALUES (45, 8);
INSERT INTO schema_course.course_topic VALUES (50, 1);
INSERT INTO schema_course.course_topic VALUES (50, 2);
INSERT INTO schema_course.course_topic VALUES (50, 3);
INSERT INTO schema_course.course_topic VALUES (50, 6);
INSERT INTO schema_course.course_topic VALUES (50, 9);
INSERT INTO schema_course.course_topic VALUES (50, 16);
INSERT INTO schema_course.course_topic VALUES (50, 18);
INSERT INTO schema_course.course_topic VALUES (50, 26);


--
-- Data for Name: course_user; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_user VALUES (2, 1, '2025-03-18 16:40:42.658', -20, '2025-03-17 16:00:01.660523', '2025-03-17 16:05:06.093105', 0, false);
INSERT INTO schema_course.course_user VALUES (1, 3, '2025-03-31 07:55:19.731', 0, '2025-03-31 07:55:22.948868', '2025-03-31 07:56:02.815067', 0, false);
INSERT INTO schema_course.course_user VALUES (45, 136, '2025-04-10 15:33:32.989', 0, NULL, NULL, 0, false);
INSERT INTO schema_course.course_user VALUES (47, 136, '2025-04-08 16:38:45.592', 0, '2025-04-13 08:27:33.770155', '2025-04-13 08:37:50.811347', 23, false);
INSERT INTO schema_course.course_user VALUES (50, 22, '2025-04-16 10:00:18.371', 0, '2025-04-16 20:19:23.05752', '2025-04-16 20:19:19.805927', 73, false);


--
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
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (35, 6, 'Lesson 1: Describe List structures', 'A list is a sequential data structure, i.e. it is a sequence of items of a given base type, where items can be added, deleted, and retrieved from any position in the list. 

A list can be implemented as an array, or as a dynamic array to avoid imposing a maximum size. 

An alternative implementation is a linked list, where the items are stored in nodes that are linked together with pointers. These two implementations have very different characteristics.

The possible values of this type are sequences of items of type BaseType (including the sequence of length zero). The operations of the ADT are: 
getFirst(), getLast(), getNext(p), getPrev(p), get(p), set(p,x), insert(p,x), remove(p),removeFirst(), removeLast(), removeNext(p), removePrev(p), find(x),size()
', 1, 'DOCUMENT', NULL, 'lessons/02e0ead2-ad2f-4b2e-96fe-3de5dfdbc03f-List data structure.pdf', '2025-04-09 04:51:30.427706', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (37, 6, 'Lesson 4: Singly Linked Lists', 'A singly linked list is a list whose node includes two datafields: info and next. The info field is used to store information, and this is important to the user. The next field is used to link to its successor in this sequence
The following image depicts a simple integer linked list.

![single linked list.png](s3:kodeholik-problem-image-13a5fb9a-a9ae-40b9-a5a6-12e71c3a747b)
', 4, 'VIDEO', 'Ovhj6qDSF9M', NULL, '2025-04-09 04:59:38.186076', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (38, 6, 'Lesson 5: Circular List', 'A circular list is when nodes form a ring: The list is finite and each node has a successor

![circular list.png](s3:kodeholik-problem-image-6f43f01d-ce24-4d80-879d-286e51e4e970)
', 5, 'VIDEO', 'videos/1744175165178_Circular Linked List _ Insert, Delete, Complexity Analysis.mp4', NULL, '2025-04-09 05:06:02.784102', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (39, 6, 'Lesson 2: Describe self-referential structures', 'Many dynamic data structures are implemented through the use of a self-referential structure.
A self-referential structure is an object, one of whose elements is a reference to another object of its own type.
With this arrangement, it is possible to create ‘chains’ of data of varying forms:
', 2, 'DOCUMENT', NULL, 'lessons/6ffee5e8-a647-421d-9dbc-04f2680254c7-Self-Referential Structures.docx', '2025-04-09 05:11:44.241318', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (36, 6, 'Lesson 1: Describe List structures', 'A list is a sequential data structure, i.e. it is a sequence of items of a given base type, where items can be added, deleted, and retrieved from any position in the list. 

A list can be implemented as an array, or as a dynamic array to avoid imposing a maximum size. 

An alternative implementation is a linked list, where the items are stored in nodes that are linked together with pointers. These two implementations have very different characteristics.

The possible values of this type are sequences of items of type BaseType (including the sequence of length zero). The operations of the ADT are: 
getFirst(), getLast(), getNext(p), getPrev(p), get(p), set(p,x), insert(p,x), remove(p),removeFirst(), removeLast(), removeNext(p), removePrev(p), find(x),size()
', 1, 'DOCUMENT', NULL, 'lessons/b87f90bf-a61f-455e-8f2b-bd23a556932f-List data structure.pdf', '2025-04-09 04:51:39.713778', 22, NULL, NULL, 'INACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (40, 6, 'Lesson 3: Types of linked lists', '
![Lesson 3 chap1.png](s3:kodeholik-problem-image-46e34385-7151-4faf-b05b-93c2e2e351be)
', 3, 'DOCUMENT', NULL, 'lessons/68c14d8b-d955-4a21-b892-03f3c7ab3f43-Type of linked list.docx', '2025-04-09 05:15:55.043283', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (41, 6, 'Lesson 6: Double Linked List', 'In a doubly linked list, each node has two reference fields, one to the successor and one to the predecessor

![Lesson 5 chap1.png](s3:kodeholik-problem-image-ef63b5f9-b578-4344-baac-b0e366883a1a)
', 6, 'DOCUMENT', NULL, 'lessons/908ea45d-155d-45ac-af66-29f090e71dfb-Doubly linked list.docx', '2025-04-09 05:20:39.43105', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (42, 7, 'Lesson 1: What is stack?', 'A stack is a linear data structure that can be accessed only at one of its ends for storing and retrieving data

A stack is a Last In, First Out (LIFO) data structure

Anything added to the stack goes on the “top” of the stack

Anything removed from the stack is taken from the “top” of the stack

Things are removed in the reverse order from that in which they were inserted

The following operations are needed to properly manage a stack:
1. clear() — Clear the stack
2. isEmpty() — Check to see if the stack is empty
3. push(el) — Put the element el on the top of the stack
4. pop() — Take the topmost element from the stack
5. top() — Return the topmost element in the stack without removing it

![lesson 1 chap2.png](s3:kodeholik-problem-image-38a15f9f-8466-4803-a510-254c34991665)

', 1, 'DOCUMENT', NULL, 'lessons/5aa84e76-db7f-4a0a-b521-927c302bf55a-What is stack.docx', '2025-04-09 14:32:58.755544', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (43, 4, 'Lesson 1: Introduction', 'Lesson 1: Introduction', 1, 'VIDEO', 'videos/1744298069280_Circular Linked List _ Insert, Delete, Complexity Analysis.mp4', NULL, '2025-04-10 15:14:26.961625', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (44, 4, 'Lesson 2: New', 'Lesson 2: New', 2, 'VIDEO', 'videos/1744298546530_videoplayback.mp4', NULL, '2025-04-10 15:16:26.350751', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (55, 16, 'Lesson 6.1: Selection Structures (if-else)', '# Welcome to Lesson 6.1!
In this guide, you’ll learn about *if-else* selection structures in C, which let your program make decisions based on conditions. These structures allow you to execute different code blocks depending on whether a condition is true or false, adding logic to your programs.

---

**What Are if-else Structures?**

*if-else* statements check conditions and choose which code to run.

They’re like decision points: "If this is true, do this; otherwise, do that."

Conditions often use relational or logical operators (e.g., `>`, `&&`).

**Example**: Check if a number is positive and print a message accordingly.

---

**if-else Syntax**
Basic forms:

```
if (condition) {
    // Code if true
}
```


```
if (condition) {
    // Code if true
} else {
    // Code if false
}
```


```
if (condition1) {
    // Code for condition1 true
} else if (condition2) {
    // Code for condition2 true
} else {
    // Code if all false
}
```


Example:

```
#include <stdio.h>

int main() {
    int num = 10;
    
    if (num > 0) {
        printf("Number is positive\n");
    } else if (num < 0) {
        printf("Number is negative\n");
    } else {
        printf("Number is zero\n");
    }
    
    return 0;
}
```


Output: `Number is positive`

Note: Use `{}` for clarity, even for single statements.

---

**Using if-else**

Example Program:

```
#include <stdio.h>

int main() {
    int score;
    printf("Enter your score: ");
    scanf("%d", &score);
    
    if (score >= 90) {
        printf("Grade: A\n");
    } else if (score >= 80) {
        printf("Grade: B\n");
    } else if (score >= 70) {
        printf("Grade: C\n");
    } else {
        printf("Grade: F\n");
    }
    
    return 0;
}
```


**Tip**: Test conditions in order (e.g., highest to lowest for grades).

---

**Key Points**

`if` checks a condition; `else` handles the false case.

Use `else if` for multiple conditions.

Conditions must evaluate to true (`1`) or false (`0`).

Clear conditions prevent logical errors.

---

**Summary**

*if-else* structures choose code based on conditions.

Use `if`, `else if`, `else` for flexible decisions.

Test conditions clearly with operators like `>` or `==`.

Happy coding!', 6, 'VIDEO', 'xyQrVEFz9kw', NULL, '2025-04-15 19:34:50.222738', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (46, 14, 'Lesson 2: Understanding Data Types', '# Welcome to Lesson 2!
In this guide, you’ll learn about data types in C, which determine the kind of data a variable can store, such as numbers or characters. Understanding data types is essential for managing memory and performing correct operations. We’ll explore the basic types, their sizes, and how to use them in your programs.

---

**What Are Data Types?**

A *data* type defines the type of value a variable can hold (e.g., integer, decimal, character).

Each type has a specific size (in bytes) and range of values.

Choosing the right type ensures efficient storage and accurate calculations.

Example: Use `int` for whole numbers like `50`, and `float` for decimals like `3.14`.

---

**Basic Data Types**

**Integer** (`int`):

Stores whole numbers (e.g., `-10`, `100`).

Size: Usually 4 bytes (system-dependent).

Example:

```
int score = 85;
printf("Score: %d\n", score);
```

**Floating-Point** (`float`, `double`):

Stores decimal numbers (e.g., `2.5`, `3.14159`).

`float`: 4 bytes, less precision.

`double`: 8 bytes, more precision.

Example:
```
float price = 9.99;
double pi = 3.14159;
printf("Price: %.2f, Pi: %.5lf\n", price, pi);
```

**Character** (`char`):

Stores single characters (e.g., `''A''`, `''9''`).

Size: 1 byte.

Example:
```
char letter = ''C'';
printf("Letter: %c\n", letter);
```

**Void** (`void`):

Indicates no value, used in functions or pointers.

Example:

```
void printHello() {
    printf("Hello, World!\n");
}
```

---

**Type Modifiers**

Modifiers like `short`, `long`, `signed`, `unsigned` change a type’s range or size:

`unsigned int`: Non-negative numbers (e.g., 0 to 4,294,967,295 for 4 bytes).

`short int`: Smaller range (e.g., 2 bytes).

Example:

```
unsigned int quantity = 500;
long int bigValue = 1000000;
```

---

**Using Data Types**
Example Program:

```
#include <stdio.h>

int main() {
    int age = 20;
    float height = 1.75;
    char initial = ''A'';
    
    printf("Age: %d years\n", age);
    printf("Height: %.2f meters\n", height);
    printf("Initial: %c\n", initial);
    
    return 0;
}
```

Output:
```
Age: 20 years
Height: 1.75 meters
Initial: A
```

**Tip**: Match the format specifier (`%d`, `%f`, `%c`) to the data type.

---

**Key Points**

Data types define what a variable can store (`int`, `float`, `char`, etc.).

Each type has a size and range, affecting memory use.

Use modifiers (`unsigned`, `long`) to adjust ranges.

Choose types carefully for correct and efficient programs.

---

**Summary**

*Data types* set the kind of data variables hold, like numbers or letters.

Use `int`, `float`, `char`, and `void` for basic needs.

Modifiers tweak size and range for flexibility.

Practice using types to store and display data correctly!

Happy coding!', 2, 'VIDEO', '1eyf1-RU_eg', NULL, '2025-04-15 16:31:34.932619', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (47, 14, 'Lesson 3: Basic Memory Operations & Input/Output Functions (scanf, printf)', '# Welcome to Lesson 3!
In this guide, you’ll learn about basic memory operations and input/output functions in C. We’ll explore how variables use memory and how to interact with users using `scanf` for input and `printf` for output. These skills let you read data from users and display results, making your programs interactive.

---

**Understanding Memory Operations**

Variables store data in memory, and each has an address.

Use the address-of operator (`&`) to get a variable’s memory location.

Memory operations let you see where and how data is stored.

Example: A variable `int x = 10;` occupies memory, and `&x` gives its address.

---

**Output with printf**

`printf` displays text and variable values to the screen.

Syntax: `printf("format string", variables);`

Common format specifiers:

`%d`: Integer

`%f`: Float

`%c`: Character

Example:

```
#include <stdio.h>

int main() {
    int age = 25;
    float height = 1.70;
    printf("Age: %d, Height: %.2f\n", age, height);
    return 0;
}
```

---

Output: `Age: 25, Height: 1.70`

**Tip**: Use `.2` in `%.2f` to limit decimal places.

---

**Input with scanf**

`scanf` reads user input from the keyboard.

Syntax: `scanf("format string", &variable);`

Use `&` to pass the variable’s address (except for strings, covered later).
Example:

```
#include <stdio.h>

int main() {
    int number;
    printf("Enter a number: ");
    scanf("%d", &number);
    printf("You entered: %d\n", number);
    return 0;
}
```

**Warning**: Match format specifiers to variable types (e.g., `%d` for `int`).

---

**Memory Addresses in Action**

Print a variable’s address with `%p` and cast to `(void*)`.

Example:

```
#include <stdio.h>

int main() {
    int x = 42;
    printf("Value: %d\n", x);
    printf("Address: %p\n", (void*)&x);
    return 0;
}
```

Output (address varies): 
```
Value: 42
Address: 0x7fff1234
```

**Why**? Understanding addresses prepares you for pointers later.

---

**Combining Input and Output**

Example Program:

```
#include <stdio.h>

int main() {
    float price;
    int quantity;
    
    printf("Enter price: ");
    scanf("%f", &price);
    printf("Enter quantity: ");
    scanf("%d", &quantity);
    
    float total = price * quantity;
    printf("Total cost: %.2f\n", total);
    return 0;
}
```

**Explanation**: Reads user input, calculates, and displays the result.

---

**Key Points**

Variables live in memory, and `&` finds their addresses.

Use `printf` with `%d`, `%f`, etc., to show data.

Use `scanf` with `&variable` to read user input.

Match format specifiers to data types for correct I/O.

---


**Summary**

*Memory operations* involve storing data and finding addresses with `&`.

*printf* displays values; *scanf* reads them from users.

Use format specifiers to handle different data types.

Practice I/O to make your programs interactive!

Happy coding!', 3, 'VIDEO', 'xOIVXR35aI4', NULL, '2025-04-15 16:54:35.85441', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (49, 15, 'Lesson 4.2: Relational Operators', '# Welcome to Lesson 4.2!
In this guide, you’ll learn about *relational* operators in C, which compare values to make decisions. These operators help you check if one number is bigger, smaller, or equal to another, forming the basis of conditions in your programs.

---

**What Are Relational Operators?**

*Relational* operators compare two values and return `1` (true) or `0` (false).

They’re used in conditions, like checking if a score is high enough.

Results are often used in `if` statements (covered later).

---

**Common Relational Operators**

**Equal to** (`==`): Checks if values are equal.

Example: `5 == 5` is `1` (true).

**Not equal to** (`!=`): Checks if values differ.

Example: `5 != 3` is `1` (true).

**Greater than** (`>`): Checks if left is larger.

Example: `5 > 3` is `1` (true).

**Less than** (`<`): Checks if left is smaller.

Example: `3 < 5` is `1` (true).

**Greater than or equal to** (`>=`): Checks if left is at least as large.

Example: `5 >= 5` is `1` (true).

**Less than or equal to** (`<=`): Checks if left is no larger.

Example: `3 <= 5` is `1` (true).

---

**Using Relational Operators**

Example Program:

```
#include <stdio.h>

int main() {
    int x = 7, y = 4;
    
    printf("x == y: %d\n", x == y);
    printf("x != y: %d\n", x != y);
    printf("x > y: %d\n", x > y);
    printf("x < y: %d\n", x < y);
    printf("x >= y: %d\n", x >= y);
    printf("x <= y: %d\n", x <= y);
    
    return 0;
}
```


Output:

```
x == y: 0
x != y: 1
x > y: 1
x < y: 0
x >= y: 1
x <= y: 0
```


**Note**: Don’t confuse `==` (comparison) with `=` (assignment).

---

**Key Points**
Relational operators (`==`, `!=`, `>`, `<`, `>=`, `<=`) compare values.

They return `1` for true, `0` for false.

Use them to test conditions in programs.

Double-check `==` vs. `=` to avoid bugs.

---

**Summary**

*Relational operators* compare values to make decisions.

Use `==`, `!=`, `>`, `<`, `>=`, `<=` for comparisons.

They return `1` or `0` for true or false.

Practice comparisons to control program flow!

Happy coding!', 4, 'VIDEO', '1oKRTjw0yuY', NULL, '2025-04-15 17:12:24.126947', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (48, 15, 'Lesson 4.1: Arithmetic Operators', '# Welcome to Lesson 4.1!
In this guide, you’ll learn about *arithmetic* operators in C, which perform basic math operations like addition and multiplication. These operators are essential for calculations in your programs. We’ll explore how to use them with numbers to solve everyday problems.

---

**What Are Arithmetic Operators?**

*Arithmetic* operators handle mathematical operations on numbers (`int`, `float`, etc.).

They include addition, subtraction, and more, working like calculator functions.

Used in expressions like `x + y`.

---

**Common Arithmetic Operators**

**Addition** (`+`): Adds two values.

Example: `5 + 3 = 8`

**Subtraction** (`-`): Subtracts one value from another.
Example: `5 - 3 = 2`

**Multiplication** (`*`): Multiplies two values.
Example: `5 * 3 = 15`

**Division** (`/`): Divides one value by another.
Example: `6 / 2 = 3`

**Note**: Integer division truncates decimals (e.g., `7 / 2 = 3`).

**Modulus** (`%`): Returns the remainder of division.
Example: `7 % 2 = 1`

Only for integers.

---

**Using Arithmetic Operators**

Example Program:

```
#include <stdio.h>

int main() {
    int a = 10, b = 3;
    
    printf("Sum: %d\n", a + b);
    printf("Difference: %d\n", a - b);
    printf("Product: %d\n", a * b);
    printf("Quotient: %d\n", a / b);
    printf("Remainder: %d\n", a % b);
    
    return 0;
}
```

Output:

```
Sum: 13
Difference: 7
Product: 30
Quotient: 3
Remainder: 1
```


**Tip**: Use `float` for decimal division (e.g., `(float)a / b`).

---

**Key Points**

Arithmetic operators are `+`, `-`, `*`, `/`, and `%`.

Integer division (`/`) drops decimals; `%` gives remainders.

Use parentheses to control operation order (e.g., `(a + b) * c`).

Match variable types to avoid unexpected results.

---

**Summary**

*Arithmetic* operators perform math like adding or dividing.

Use `+`, `-`, `*`, `/`, `%` for calculations.

Watch for integer division and type mismatches.

Practice math operations to build computational skills!

Happy coding!', 4, 'VIDEO', '5JXcX0IqRUo', NULL, '2025-04-15 17:06:41.263193', 22, '2025-04-15 17:12:34.332302', 22, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (56, 16, 'Lesson 6.2: Selection Structures (switch-case)', '# Welcome to Lesson 6.2!
In this guide, you’ll learn about *switch-case* selection structures in C, an alternative to `if-else` for handling multiple choices. Switch-case makes your code cleaner when checking a variable against specific values, like selecting menu options.

---

**What Are switch-case Structures?**

*switch-case* tests a variable against fixed values and runs matching code.

It’s ideal for discrete choices (e.g., integers, characters).

Cleaner than long `if-else` chains for specific cases.

**Example**: Choose an action based on a menu number.

---

**switch-case Syntax**

Structure:

```
switch (expression) {
    case value1:
        // Code for value1
        break;
    case value2:
        // Code for value2
        break;
    default:
        // Code if no match
}
```


`break` stops execution; without it, cases "fall through."

`default` handles unmatched values (optional).

Example:

```
#include <stdio.h>

int main() {
    int choice = 2;
    
    switch (choice) {
        case 1:
            printf("Option 1 selected\n");
            break;
        case 2:
            printf("Option 2 selected\n");
            break;
        case 3:
            printf("Option 3 selected\n");
            break;
        default:
            printf("Invalid choice\n");
    }
    
    return 0;
}
```


Output: `Option 2 selected`

Note: `expression` must evaluate to an integer or character.

---

**Using switch-case**

Example Program:

```
#include <stdio.h>

int main() {
    char grade;
    printf("Enter your grade (A–F): ");
    scanf(" %c", &grade);
    
    switch (grade) {
        case ''A'':
            printf("Excellent!\n");
            break;
        case ''B'':
            printf("Good job!\n");
            break;
        case ''C'':
            printf("You passed.\n");
            break;
        case ''F'':
            printf("Try harder.\n");
            break;
        default:
            printf("Invalid grade\n");
    }
    
    return 0;
}
```


Tip: Add `break` to avoid executing later cases.

---

**Key Points**

`switch-case` matches a variable to `case` values.

Use `break` to exit after a case.

`default` catches unexpected inputs.

Best for integers or characters, not ranges.

---

**Summary**

*switch-case* simplifies multiple-choice decisions.

Match values with `case` and exit with `break`.

Use `default` for error handling.

Happy coding!', 6, 'VIDEO', 'tjd8fQw5HTA', NULL, '2025-04-15 19:41:05.053024', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (50, 15, 'Lesson 4.3: Logical Operators', '# Welcome to Lesson 4.3!
In this guide, you’ll learn about *logical operators* in C, which combine conditions to make complex decisions. These operators let you check multiple criteria at once, like whether a number is both positive and even.

---

**What Are Logical Operators?**

*Logical operators* work with boolean values (`1` or `0`) to combine or modify conditions.

They’re used to test multiple relational expressions together.

Return `1` (true) or `0` (false).

---

**Common Logical Operators**

**AND** (`&&`): True if both conditions are true.

Example: `(5 > 3) && (4 < 6)` is `1` (true).

**OR** (`||`): True if at least one condition is true.
Example: `(5 > 6) || (4 < 6)` is `1` (true).

**NOT** (`!`): Reverses a condition (true becomes false, false becomes true).
Example: `!(5 > 3)` is `0` (false).

---

**Using Logical Operators**

Example Program:

```
#include <stdio.h>

int main() {
    int x = 10, y = 5;
    
    printf("x > 5 && y < 10: %d\n", x > 5 && y < 10);
    printf("x > 15 || y < 10: %d\n", x > 15 || y < 10);
    printf("!(x == y): %d\n", !(x == y));
    
    return 0;
}
```


Output:

```
x > 5 && y < 10: 1
x > 15 || y < 10: 1
!(x == y): 1
```


**Tip**: Use parentheses to clarify complex conditions (e.g., `(x > 5) && (y < 10)`).

---

**Key Points**

Logical operators are `&&`, `||`, and `!`.

`&&` needs both true; `||` needs one true; `!` flips the truth.

They combine relational operators for richer logic.

Parentheses ensure the right evaluation order.

---

**Summary**

*Logical operators* combine conditions with `&&`, `||`, and `!`.

Use them to test multiple criteria at once.

They return `1` for true, `0` for false.

Practice logical checks to make smarter programs!

Happy coding!', 4, 'VIDEO', 'WGQRInmOBM8', NULL, '2025-04-15 17:17:02.136213', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (51, 15, 'Lesson 4.4: Bitwise Operators', '# Welcome to Lesson 4.4!
In this guide, you’ll learn about *bitwise operators* in C, which manipulate individual bits of numbers. These operators are useful for low-level programming, like optimizing code or working with hardware.

---

**What Are Bitwise Operators?**

*Bitwise operators* work on binary representations of integers (e.g., `5` is `0101`).

They perform operations like shifting or flipping bits.

Common in system programming, not everyday math.

Example: `5 & 3` compares bits of `0101` and `0011`.

---

**Common Bitwise Operators**

**AND** (`&`): Sets a bit to 1 if both bits are 1.

Example: `5 & 3` (`0101 & 0011 = 0001`) is `1`.

**OR** (`|`): Sets a bit to 1 if either bit is 1.

Example: `5 | 3` (`0101 | 0011 = 0111`) is `7`.

**XOR** (`^`): Sets a bit to 1 if only one bit is 1.

Example: `5 ^ 3` (`0101 ^ 0011 = 0110`) is `6`.

**NOT** (`~`): Flips all bits (1 to 0, 0 to 1).

Example: `~5` (`~0101 = 1010` in 32-bit) depends on system.

**Left Shift** (`<<`): Shifts bits left, filling with zeros.

Example: **5 << 1** (`0101 << 1 = 1010`) is `10`.

**Right Shift** (`>>`): Shifts bits right, filling based on sign.

Example: `5 >> 1` (`0101 >> 1 = 0010`) is `2`.

---

**Using Bitwise Operators**

Example Program:

```
#include <stdio.h>

int main() {
    int a = 5, b = 3; // 0101, 0011
    
    printf("a & b: %d\n", a & b);
    printf("a | b: %d\n", a | b);
    printf("a ^ b: %d\n", a ^ b);
    printf("~a: %d\n", ~a);
    printf("a << 1: %d\n", a << 1);
    printf("a >> 1: %d\n", a >> 1);
    
    return 0;
}
```


Output:

```
a & b: 1
a | b: 7
a ^ b: 6
~a: -6
a << 1: 10
a >> 1: 2
```

**Note**: `~` results depend on integer size (e.g., 32-bit).

---

**Key Points**

Bitwise operators (`&`, `|`, `^`, `~`, `<<`, `>>`) work on bits.

Use for low-level tasks, not regular math.

Shifts multiply (`<<`) or divide (`>>`) by powers of `2`.

Understand binary to predict results.

---

**Summary**

*Bitwise operators* manipulate binary bits for special tasks.

Use `&`, `|`, `^`, `~`, `<<`, `>>` to control bits.

They’re powerful for optimization and hardware.

Practice bitwise operations to understand binary!

Happy coding!', 4, 'VIDEO', 'BGeOwlIGRGI', NULL, '2025-04-15 17:27:01.27019', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (53, 15, 'Lesson 5.1: Mixing Data Types, Casting', '# Welcome to Lesson 5.1!
In this guide, you’ll learn about *mixing data types* and *casting* in C. Mixing data types lets you combine int, float, and other types in calculations, while casting allows you to convert types explicitly. These skills help you perform accurate computations and avoid common errors.

---

**Mixing Data Types**

Mixing types occurs when you operate on different types, like adding an `int` to a `float`.

C uses *type promotion* to automatically convert smaller types (e.g., `int`) to larger ones (e.g., `float`).

Example:

```
int x = 10;
float y = 3.5;
float result = x + y; // x becomes float
printf("Result: %.2f\n", result); // 13.50
```


Caution: Integer operations (e.g., `5 / 2`) truncate unless promoted.

---

**Casting**

*Casting* forces a value to a specific type using `(type)expression`.

Useful for controlling results or assignments.

Example:

```
float a = 7.9;
int b = (int)a; // Truncates to 7
printf("b: %d\n", b);
```


Common use: Force decimal division:

```
int x = 5, y = 2;
float div = (float)x / y; // 2.50, not 2
printf("Division: %.2f\n", div);
```


**Note**: Casting to `int` truncates, not rounds.

---

**Using Mixing and Casting**
Example Program:

```
#include <stdio.h>

int main() {
    int count = 8;
    float avg = 4.75;
    
    // Mixing
    float total = count * avg;
    
    // Casting
    int rounded = (int)total;
    
    printf("Total: %.2f\n", total);
    printf("Rounded: %d\n", rounded);
    
    return 0;
}
```


Output:

```
Total: 38.00
Rounded: 38
```


**Tip**: Cast when you need specific type behavior.

---

**Key Points**
Mixing types promotes smaller types to larger ones (e.g., `int` to `float`).

Casting with `(type)` gives you control over conversions.

Use casting to avoid truncation in division or assignments.

Check results to ensure accuracy.

---

**Summary**

*Mixing data* types combines types with automatic promotion.

*Casting* explicitly converts types for precision.

Use both to control calculations and outputs.

Happy coding!', 5, 'VIDEO', 'xKOPZ4mEH3I', NULL, '2025-04-15 17:48:55.261189', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (54, 15, 'Lesson 5.2: Operator Precedence', '# Welcome to Lesson 5.2!
In this guide, you’ll learn about *operator precedence* in C, which determines the order in which operators are evaluated in expressions. Understanding precedence helps you write correct calculations and use parentheses to control outcomes, avoiding surprises in your code.

---

**What Is Operator Precedence?**

*Operator precedence* sets which operators are applied first in an expression.

Example: In `2 + 3 * 4`, `*` is evaluated before `+`.

Parentheses `()` override precedence for clarity.

**Why It Matters**: Wrong precedence assumptions lead to incorrect results.

---

**Precedence Rules**

Simplified precedence (higher to lower):

`*`, `/`, `%` (multiplicative)

`+`, `-` (additive)

`=` (assignment)

Equal precedence operators (e.g., `*` and `/`) evaluate left to right.

Example:

```
int x = 10 - 4 / 2; // / first: 4 / 2 = 2, then 10 - 2 = 8
int y = (10 - 4) / 2; // () first: 6 / 2 = 3
printf("x: %d, y: %d\n", x, y);
```


---

**Using Parentheses**

Parentheses force evaluation order, making code clearer.

Example:

```
#include <stdio.h>

int main() {
    int a = 5, b = 3, c = 2;
    
    int result1 = a + b * c; // * first: 3 * 2 = 6, 5 + 6 = 11
    int result2 = (a + b) * c; // () first: 5 + 3 = 8, 8 * 2 = 16
    
    printf("Result1: %d\n", result1);
    printf("Result2: %d\n", result2);
    
    return 0;
}
```


Output:

```
Result1: 11
Result2: 16
```


**Tip**: Use parentheses even when not required to improve readability.

---

**Key Points**

Operator precedence decides evaluation order (`*`, `/` before `+`, `-`).

Parentheses `()` override precedence for control.

Left-to-right evaluation applies for equal precedence.

Clear precedence avoids calculation errors.

---

**Summary**

*Operator precedence* controls how expressions are computed.

`*`, `/`, `%` come before `+`, `-`; `=` is last.

Use parentheses to enforce your desired order.

Happy coding!', 5, 'VIDEO', '8H9G621pQq0', NULL, '2025-04-15 17:53:39.079415', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (57, 16, 'Lesson 7.1: Loop Structures (for)', '# Welcome to Lesson 7.1!
In this guide, you’ll learn about the *for* loop structure in C, which lets you repeat code a specific number of times. The `for` loop is perfect when you know how many iterations you need, like counting from 1 to 10. We’ll explore its syntax and use it to simplify repetitive tasks.

---

**What Is a for Loop?**

A *for* loop repeats a block of code for a set number of times.

It combines initialization, condition, and update in one line.

Ideal for tasks like summing numbers or printing sequences.

**Example**: Print numbers 1 to 5.

---

**for Loop Syntax**

Structure:

```
for (initialization; condition; update) {
    // Code to repeat
}
```


`initialization`: Sets up a counter (e.g., `int i = 0`).

`condition`: Checked before each loop (e.g., `i < 5`).

`update`: Changes the counter (e.g., `i++`).

Example:

```
#include <stdio.h>

int main() {
    for (int i = 1; i <= 5; i++) {
        printf("%d ", i);
    }
    printf("\n");
    return 0;
}
```


Output: `1 2 3 4 5`

**Note**: The loop stops when the condition is false.

---

**Using for Loops**

Example Program:

```
#include <stdio.h>

int main() {
    int sum = 0;
    for (int i = 1; i <= 10; i++) {
        sum += i; // Add i to sum
    }
    printf("Sum of 1 to 10: %d\n", sum);
    return 0;
}
```


Output: `Sum of 1 to 10: 55`

**Tip**: Use `for` when the number of loops is known upfront.

---

**Key Points**

`for` loops need initialization, condition, and update.

The loop runs while the condition is true.

Use for counting or fixed iterations.

Keep counters clear to avoid infinite loops.

---

**Summary**

*for* loops repeat code for a known number of times.

Use `for (init; condition; update)` for clear structure.

Perfect for counting and summing tasks.

Practice `for` loops to handle repetition easily!
Happy coding!', 7, 'VIDEO', 'b4DPj0XAfSg', NULL, '2025-04-15 19:48:06.916806', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (58, 16, 'Lesson 7.2: Loop Structures (while)', '# Welcome to Lesson 7.2!
In this guide, you’ll learn about the *while* loop structure in C, which repeats code as long as a condition is true. The `while` loop is great when you don’t know exactly how many iterations you’ll need, like reading input until a specific value.

---

**What Is a while Loop?**

A *while* loop runs a block of code while a condition remains true.

It checks the condition before each iteration.

Useful for dynamic tasks, like processing user input.

**Example**: Keep doubling a number until it exceeds 100.

---

**while Loop Syntax**

Structure:

```
while (condition) {
    // Code to repeat
}
```


`condition`: Must be true (`1`) to continue looping.

Example:

```
#include <stdio.h>

int main() {
    int i = 1;
    while (i <= 5) {
        printf("%d ", i);
        i++;
    }
    printf("\n");
    return 0;
}
```


Output: `1 2 3 4 5`

**Note**: Update the condition variable (e.g., `i++`) to avoid infinite loops.

---

**Using while Loops**

Example Program:

```
#include <stdio.h>

int main() {
    int num = 1;
    while (num <= 100) {
        printf("%d ", num);
        num *= 2; // Double the number
    }
    printf("\n");
    return 0;
}
```


Output: `1 2 4 8 16 32 64`

**Tip**: Use `while` when the loop count depends on a condition.

---

**Key Points**

`while` loops run as long as the condition is true.

Check conditions before each loop.

Update variables to ensure the loop ends.

Great for uncertain iteration counts.

---

**Summary**

*while* loops repeat based on a condition.

Use `while (condition)` for flexible looping.

Ensure conditions change to prevent infinite loops.

Happy coding!', 7, 'VIDEO', 'ufFAFx5Qn3w', NULL, '2025-04-15 19:51:52.306985', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (59, 16, 'Lesson 7.3: Loop Structures (do-while)', '# Welcome to Lesson 7.3!
In this guide, you’ll learn about the *do-while* loop structure in C, which guarantees at least one execution before checking a condition. The `do-while` loop is ideal for tasks where you need to run code first, like prompting users until valid input.

---

**What Is a do-while Loop?**

A *do-while* loop executes code, then checks a condition to decide whether to repeat.

Unlike `while`, it runs at least once.

Perfect for menus or input validation.

**Example**: Ask for a positive number, looping until valid.

---

**do-while Loop Syntax**

Structure:

```
do {
    // Code to repeat
} while (condition);
```


`condition`: Checked *after* each loop; must be true to continue.

Example:

```
#include <stdio.h>

int main() {
    int i = 1;
    do {
        printf("%d ", i);
        i++;
    } while (i <= 5);
    printf("\n");
    return 0;
}
```


Output: `1 2 3 4 5`

Note: The semicolon `;` after `while` is required.

---

**Using do-while Loops**

Example Program:

```
#include <stdio.h>

int main() {
    int num;
    do {
        printf("Enter a positive number: ");
        scanf("%d", &num);
    } while (num <= 0);
    printf("You entered: %d\n", num);
    return 0;
}
```

**Tip**: Use `do-while` when the code must run at least once.

---

**Key Points**

`do-while` runs code before checking the condition.

Guarantees at least one iteration.

Needs a `;` after `while (condition)`.

Best for input validation or single-run tasks.

---

**Summary**

*do-while* loops run at least once, then check conditions.

Use `do { ... } while (condition);` for guaranteed execution.

Ideal for validation or menu loops.

Happy coding!', 7, 'VIDEO', 'BIiE6LNy6Ck', NULL, '2025-04-15 19:55:29.428363', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (60, 17, 'Lesson 8: Function Definitions', '# Welcome to Lesson 8!
In this guide, you’ll learn about *function definitions* in C, which allow you to create reusable blocks of code to perform specific tasks. Functions make your programs modular, easier to read, and simpler to debug. We’ll explore how to define, declare, and use functions effectively.

---

**What Are Function Definitions?**

A *function definition* specifies what a function does, including its name, inputs, output, and code.

Functions break down complex programs into smaller, manageable pieces.

Example: A function to calculate the square of a number can be reused anywhere.

---

**Function Definition Syntax**

Structure:

```
return_type function_name(parameter_list) {
    // Code
    return value; // If return_type isn’t void
}
```


`return_type`: Type of value returned (e.g., `int`, `float`, `void`).

`function_name`: Unique name (e.g., `add`, `printMessage`).

`parameter_list`: Variables the function accepts (e.g., `int x, float y`).

Example:

```
int multiply(int a, int b) {
    return a * b;
}
```


**Note**: Use `void` for functions that don’t return anything:

```
void sayHello() {
    printf("Hello, World!\n");
}
```

---

**Declaring and Calling Functions**

*Declare* a function (prototype) before `main` if defined later:

```
return_type function_name(parameter_list);
```


*Call* a function by its name with arguments.

Example:

```
#include <stdio.h>

int add(int x, int y); // Prototype

int main() {
    int result = add(3, 4); // Call
    printf("Sum: %d\n", result);
    return 0;
}

int add(int x, int y) { // Definition
    return x + y;
}
```


Output: `Sum: 7`

**Tip**: Prototypes ensure the compiler knows about the function.

---

**Using Functions**

Example Program:

```
#include <stdio.h>

float areaCircle(float radius) {
    return 3.14 * radius * radius;
}

int main() {
    float r = 5.0;
    printf("Area of circle: %.2f\n", areaCircle(r));
    return 0;
}
```


Output: `Area of circle: 78.50`

**Tip**: Use meaningful function names for clarity (e.g., `areaCircle`).

---

**Key Points**

Define functions with `return_type`, name, and parameters.

Use `return` to send back values or `void` for no return.

Declare prototypes if defining functions after `main`.

Functions make code reusable and organized.

---

**Summary**
*Function definitions* create reusable code blocks.

Use `return_type name(parameters)` to define them.

Prototypes and calls make functions flexible.

Happy coding!', 8, 'VIDEO', 'gF7wjwM9Jjs', NULL, '2025-04-15 20:01:09.226188', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (61, 17, 'Lesson 9: Using Functions and Pass by Value', '# Welcome to Lesson 9!
In this guide, you’ll learn how to use *functions* in C and understand pass by value for sending data to them. Functions let you reuse code, and pass by value ensures functions work with copies of data, keeping original variables safe. We’ll explore how to call functions and manage parameters effectively.

---

**What Are Functions and Pass by Value?**
Functions are reusable code blocks that perform specific tasks, like calculating or printing.

*Pass by value* means functions receive copies of arguments, not the original variables.

Changes to parameters inside a function don’t affect the caller’s variables.

**Example**: A function to double a number works on a copy, leaving the original unchanged.

---

**Using Functions**

Call a function by its name with arguments:

```
result = function_name(arg1, arg2);
```


Functions need a *prototype* or definition before use.

Example:

```
#include <stdio.h>

int square(int num); // Prototype

int main() {
    int x = 4;
    printf("Square of %d: %d\n", x, square(x));
    return 0;
}

int square(int num) {
    return num * num;
}
```


Output: `Square of 4: 16`

**Note**: The prototype tells the compiler about the function’s return type and parameters.

---

**Pass by Value**

Parameters receive copies of arguments, so changes inside the function are local.

Example:

```
#include <stdio.h>

void tryChange(int value) {
    value = 100; // Changes copy, not original
    printf("Inside function: %d\n", value);
}

int main() {
    int x = 5;
    printf("Before: %d\n", x);
    tryChange(x);
    printf("After: %d\n", x);
    return 0;
}
```


Output:

```
Before: 5
Inside function: 100
After: 5
```

**Why**? `value` is a copy of `x`, so `x` stays unchanged.

---

**Practical Example**

Combining functions and pass by value:

```
#include <stdio.h>

float calculateTotal(float price, int quantity) {
    return price * quantity; // Return computed value
}

int main() {
    float p = 2.5;
    int q = 3;
    float total = calculateTotal(p, q);
    printf("Price: %.2f, Quantity: %d\n", p, q);
    printf("Total: %.2f\n", total);
    return 0;
}
```


Output:

```
Price: 2.50, Quantity: 3
Total: 7.50
```

**Tip**: Pass by value keeps original data safe but limits direct modifications.

**Key Points**

Functions are called with arguments matching their parameters.

Pass by value sends copies, protecting original variables.

Use prototypes for functions defined after `main`.

Functions make code modular and reusable.

---

**Summary**

*Functions* run reusable code with specific inputs.

*Pass by value* uses copies, keeping originals unchanged.

Call functions with proper arguments for results.

Happy coding!', 9, 'VIDEO', 'HEiPxjVR8CU', NULL, '2025-04-15 20:09:17.906865', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (62, 17, 'Lesson 10: Scope and Lifetime of Variables', '# Welcome to Lesson 10!
In this guide, you’ll learn about the *scope* and *lifetime* of variables in C. Scope determines where a variable can be used, and lifetime defines how long it exists in memory. Understanding these concepts helps you manage variables effectively and avoid common errors in your programs.

---

**What Are Scope and Lifetime?**

*Scope*: The region of code where a variable is accessible (e.g., inside a function or globally).

*Lifetime*: The duration a variable retains its value in memory (e.g., during a function call or the entire program).

Misusing scope or lifetime can cause bugs, like accessing a variable that no longer exists.

**Example**: A variable declared inside a function isn’t available outside it.

---

**Variable Scope**

**Local Scope**:

Variables declared inside a function or block `{}`.

Accessible only within that function or block.

Example:

```
void myFunction() {
    int x = 5; // Local to myFunction
    printf("x: %d\n", x);
}
```


**Global Scope**:

Variables declared outside all functions.

Accessible everywhere in the program.

Example:

```
int global = 10; // Global variable
```


**Block Scope**:

Variables declared inside `{}` (e.g., in a loop).

Limited to that block.

Example:

```
for (int i = 0; i < 3; i++) {
    printf("i: %d\n", i); // i only exists here
}

```

**Note**: Same-name variables in different scopes don’t conflict—local overrides global.

---

**Variable Lifetime**

**Automatic Lifetime**:

Local variables (default).

Created when entering their scope, destroyed when leaving.

Example: `int x` in a function exists only during the function call.

**Static Lifetime:**

Variables declared with `static` or global variables.

Exist for the entire program’s duration.

Example:

```
static int count = 0; // Retains value between calls
```


**Global Lifetime**:

Global variables live from program start to end.

---

**Using Scope and Lifetime**

Example Program:

```
#include <stdio.h>

int globalVar = 100; // Global, lives entire program

void counter() {
    static int calls = 0; // Static, retains value
    int local = 5; // Automatic, resets each call
    calls++;
    local++;
    printf("Calls: %d, Local: %d\n", calls, local);
}

int main() {
    printf("Global: %d\n", globalVar);
    counter(); // Call 1
    counter(); // Call 2
    {
        int blockVar = 20; // Block scope
        printf("Block: %d\n", blockVar);
    }
    // blockVar inaccessible here
    return 0;
}
```


Output:

```
Global: 100
Calls: 1, Local: 6
Calls: 2, Local: 6
Block: 20
```

**Explanation**: `globalVar` is always available, `calls` persists, `local` resets, `blockVar` is limited.

---

**Key Points**

*Scope* limits where variables are usable (local, global, block).

*Lifetime* sets how long variables exist (automatic, static).

Local variables are temporary; static and global persist.

Use scope carefully to avoid naming conflicts or errors.

---

**Summary**

*Scope* defines variable accessibility (local, global, block).

*Lifetime* controls variable duration (automatic, static).

Use local for temporary data, static for persistence.

Happy coding!', 10, 'VIDEO', 'elMQ5YtZPxA', NULL, '2025-04-15 20:35:36.48742', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (63, 18, 'Lesson 11: Pointer Declarations and Operators', '# Welcome to Lesson 11! 
In this guide, you’ll dive into the world of pointers in C—a powerful but sometimes tricky concept. You’ll learn what pointers are, how they work, and how to use the `*` and `&` operators effectively. Let’s get started!

**What Are Pointers?**

A pointer is a variable that stores the memory address of another variable.

Instead of holding a value like int or float, a pointer holds the location (address) where a value is stored.

Why use pointers?

- They allow direct memory manipulation.
- They enable functions to modify variables (via pass-by-reference).
- They’re essential for dynamic memory allocation and arrays.
 
**Example Concept:**

If you have a variable int x = 10;, a pointer can store the address of x rather than the value 10.

---

**Pointer Operators: * and &**

C uses two key operators for pointers:

**`&` (Address-of Operator):**

Returns the memory address of a variable.

Syntax: `&variable`

Example:
```
int x = 10;
printf("Address of x: %p\n", &x);

```
Output: A memory address (e.g., `0x7fff1234`).

**`*`(Dereference Operator):**

Accesses the value stored at the address held by a pointer.

Syntax: `*pointer`

Used in both declaration (to define a pointer) and dereferencing (to get/set the value).

---

**Declaring Pointers**
Syntax: `type *pointer_name;`

`type`: The type of data the pointer points to (e.g., int, float).

`*`: Indicates it’s a pointer.

`pointer_name`: The name of the pointer variable.

Example:
```
int *ptr; // Declares a pointer to an int
```

Important: A pointer must be initialized before use, or it might point to random memory (causing errors).

---

**Using Pointers: Step-by-Step Example**

Here’s a complete program showing how to declare and use pointers:

```
#include <stdio.h>

int main() {
 int x = 10; // A regular variable
 int *ptr; // Declare a pointer to int
 ptr = &x; // Store address of x in ptr
 
 printf("Value of x: %d\n", x); // Prints: 10
 printf("Address of x: %p\n", &x); // Prints: address
 printf("Pointer value (address): %p\n", ptr); // Same address
 printf("Value at pointer: %d\n", *ptr); // Prints: 10 (dereferenced)
 
 *ptr = 20; // Change value at address
 printf("New value of x: %d\n", x); // Prints: 20
 
 return 0;
}
```

Explanation:

`ptr = &x;` assigns the address of `x` to `ptr`.

`*ptr` accesses or modifies the value at that address (`x` in this case).

---

**Common Pitfalls**
Uninitialized Pointers: Declaring `int *ptr;` without assigning an address can cause crashes.
```
int *ptr;
*ptr = 5; // BAD! ptr points to random memory
```

Dangling Pointers: A pointer that points to memory that’s been freed or out of scope.

Type Mismatch: Ensure the pointer type matches the variable type (e.g., don’t point an int* to a float).

---

**Summary**

Pointers store memory addresses, letting you work directly with a variable’s location.

Use `&` to get an address and `*` to access or modify the value at that address.

Declare pointers carefully and always initialize them to avoid errors.

Practice is key—pointers get easier with hands-on coding!

Happy coding!', 11, 'VIDEO', 'DplxIq0mc_Y', NULL, '2025-04-15 20:38:18.798251', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (64, 18, 'Lesson 12: Passing Pointers to Functions (Pass by Reference)', '# Welcome to Lesson 12!

In this guide, you’ll learn how to pass pointers to functions in C, enabling pass-by-reference. This technique allows functions to modify the original variables passed to them. We’ll explore the difference between pass-by-value and pass-by-reference and see how to use pointer parameters to change values.


---
**Understanding Pass-by-Value vs. Pass-by-Reference**

**Pass-by-Value:**

The function receives a copy of the argument.

Changes to parameters inside the function do not affect the original variable.

Example:

```
void tryChange(int x) {
 x = 100;
}
int main() {
 int num = 5;
 tryChange(num);
 printf("Num: %d\n", num); // Still 5
 return 0;
}
```

**Pass-by-Reference:**

The function receives a pointer to the original variable’s address.

Changes to the dereferenced pointer (`*parameter`) affect the original variable.

Achieved by passing pointers to functions.

---

**Passing Pointers to Functions**
To modify a variable, pass its address (using &) to a function that accepts a pointer parameter.

Syntax:

Function declaration: `void functionName(type *parameter);`

Function call: `functionName(&variable);`

Example:

```
#include <stdio.h>

void increment(int *p) {
 *p = *p + 1; // Modify value at address
}

int main() {
 int x = 10;
 increment(&x);
 printf("X: %d\n", x); // Prints: 11
 return 0;
}
```

**Explanation:** The function `increment` receives the address of `x`, and `*p` modifies the value at that address.

---

**Modifying Values Using Pointer Parameters**

Pointers let functions change multiple variables or return multiple results indirectly. Here’s a practical example:

**Swapping Two Variables:**

```
#include <stdio.h>

void swap(int *a, int *b) {
 int temp = *a; // Get value at a
 *a = *b; // Set a to value at b
 *b = temp; // Set b to temp
}

int main() {
 int x = 5, y = 10;
 printf("Before: x=%d, y=%d\n", x, y);
 swap(&x, &y);
 printf("After: x=%d, y=%d\n", x, y);
 return 0;
}
```

Output:
```
Before: x=5, y=10
After: x=10, y=5
```

**Why It Works:** The function modifies the original `x` and `y` by accessing their addresses.

---

**Key Points**

**Pass-by-Value:** Safe but can’t change originals.

**Pass-by-Reference:** Powerful for modifying variables but requires careful pointer handling.

Always check that pointers are valid (not `NULL`) before dereferencing.

Use `const` if you want to prevent accidental changes:

```
void printValue(const int *p) {
 // *p = 10; // Error: cannot modify
 printf("Value: %d\n", *p);
}
```

---

**Summary**

Pass-by-value copies data; pass-by-reference uses pointers to modify originals.

Pass pointers to functions with `&variable` and declare parameters as `type *parameter`.

Use `*parameter` to access or change the value at the address.

Pointers enable flexible and efficient variable manipulation—practice makes perfect!

Happy coding!', 12, 'VIDEO', 'DplxIq0mc_Y', NULL, '2025-04-15 20:39:04.265506', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (65, 18, 'Lesson 13: Dynamically Allocated Memory', '# Welcome to Lesson 13! 
In this guide, you’ll learn how to manage memory dynamically in C using malloc and free. Dynamic memory allocation lets you request memory at runtime and release it when done, giving you flexibility for data like arrays or structures. Let’s dive in!

---

**What Is Dynamic Memory Allocation?**

Unlike static variables (e.g., `int arr[10];`), dynamic memory is allocated during program execution.

It’s useful when you don’t know the size of data in advance (e.g., user input).

C provides functions like `malloc` and `free` to control memory.

---

**Using malloc to Allocate Memory**

**malloc** (memory allocation) reserves a block of memory and returns a pointer to it.

Syntax: `pointer = (type *)malloc(size_in_bytes);`

`size_in_bytes`: Total bytes needed (e.g., `n * sizeof(type)`).

Returns a `void*`, so cast it to the desired type (e.g., `int*`).

Example:

```
#include <stdio.h>
#include <stdlib.h>

int main() {
 int *arr;
 int n = 5;
 arr = (int *)malloc(n * sizeof(int)); // Allocate for 5 integers
 
 if (arr == NULL) {
 printf("Memory allocation failed!\n");
 return 1;
 }
 
 for (int i = 0; i < n; i++) {
 arr[i] = i + 1;
 }
 
 printf("Array: ");
 for (int i = 0; i < n; i++) {
 printf("%d ", arr[i]);
 }
 printf("\n");
 
 return 0;
}
```

Output: `Array: 1 2 3 4 5`

**Explanation:** `malloc` allocates space for `n` integers, which you can use like an array.

---

**Freeing Memory with free**

**free** releases memory allocated by `malloc` to prevent memory leaks.

Syntax: `free(pointer);`

Example (continuing from above):

```
free(arr); // Release the allocated memory
arr = NULL; // Optional: prevents accidental use
```

**Why It Matters:** Failing to free memory can waste system resources.

---

**Key Points**

**malloc:**

Returns `NULL` if allocation fails—always check for this.

Use `sizeof` to calculate the correct size (portable across systems).

**free:**

Only free memory you allocated dynamically.

Don’t use a pointer after freeing it (set to `NULL` to be safe).

Dynamic memory is stored in the heap, unlike local variables (stack).

---

**Common Pitfalls**

**Memory Leaks:** Forgetting to call `free`:
```
int *ptr = (int *)malloc(10 * sizeof(int));
ptr = NULL; // Leak! Memory can’t be freed now
```

**Double Free:** Calling `free` twice on the same pointer causes errors.

**Accessing Freed Memory:** Using a pointer after free is undefined behavior.

---

**Summary**

*malloc* allocates memory dynamically; *free* releases it.

Check for `NULL` after `malloc` to handle allocation failures.

Use dynamic memory for flexible data sizes, but manage it carefully to avoid leaks.

Practice allocating and freeing memory to build confidence!

Happy coding!', 13, 'VIDEO', 'R0qIYWo8igs', NULL, '2025-04-15 20:44:52.367195', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (66, 19, 'Lesson 14: One-Dimensional Arrays and Basic Operations', '# Welcome to Lesson 14!
In this guide, you’ll learn how to declare and use one-dimensional arrays in C. Arrays are a powerful way to store multiple values of the same type in a single variable. We’ll cover how to create arrays, access their elements, and perform basic operations like initialization and traversal.

---

**What Is a One-Dimensional Array?**

An *array* is a collection of elements, all of the same data type, stored in contiguous memory.

A *one-dimensional* array is like a list of values (e.g., a row of numbers).

Arrays are indexed, starting at 0 for the first element.

**Example Concept:**

If you want to store 5 scores, instead of `score1`, `score2`, etc., you can use one array: `scores[5]`.

---

**Declaring Arrays**

Syntax: `type array_name[size];`

`type`: Data type of elements (e.g., `int`, `float`).

`array_name`: Name of the array.

`size`: Number of elements (fixed at declaration).

Example:

```
int numbers[4]; // Array for 4 integers
float prices[3]; // Array for 3 floats
```

**Important:** The size must be a constant or known at compile time (in basic C).

---

**Initializing Arrays**

You can set values when declaring an array or later.

At Declaration:

```
int ages[5] = {10, 20, 30, 40, 50}; // Initialize with values
int zeros[3] = {0}; // All elements set to 0
```


After Declaration:

```
int data[3];
data[0] = 1;
data[1] = 2;
data[2] = 3;
```


**Note**: Uninitialized elements may contain random values—always initialize arrays.

---

**Accessing Array Elements**

Use the index (starting from `0`) to access or modify elements.

Syntax: `array_name[index]`

Example:
```
#include <stdio.h>

int main() {
 int scores[4] = {85, 90, 78, 92};
 printf("First score: %d\n", scores[0]); // Prints: 85
 scores[2] = 80; // Change third element
 printf("New third score: %d\n", scores[2]); // Prints: 80
 return 0;
}
```

**Warning**: Accessing beyond the array size (e.g., `scores[4]` for a 4-element array) causes undefined behavior.

---

**Basic Operations: Traversing Arrays**

Use loops (like `for`) to process array elements.

Example (Printing and Summing):

```
#include <stdio.h>

int main() {
 int values[5] = {1, 2, 3, 4, 5};
 int sum = 0;
 
 // Print array
 printf("Array: ");
 for (int i = 0; i < 5; i++) {
 printf("%d ", values[i]);
 }
 printf("\n");
 
 // Calculate sum
 for (int i = 0; i < 5; i++) {
 sum += values[i];
 }
 printf("Sum: %d\n", sum);
 
 return 0;
}
```

Output:
```
Array: 1 2 3 4 5
Sum: 15
```

---

**Key Points**

Arrays store multiple values of the same type in order.

Indexes start at `0`, so an array of size `n` goes from `0` to `n-1`.

Initialize arrays to avoid unpredictable values.

Use loops for operations like printing, summing, or updating elements.

---

**Summary**

*Arrays* are lists of same-type elements, accessed by index.

Declare with `type name[size]` and initialize manually or at creation.

Use loops for common tasks like traversal or calculations.

Practice array operations to handle data efficiently!

Happy coding!', 14, 'VIDEO', 'eE9MnoS0lc0', NULL, '2025-04-16 20:12:31.774704', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (52, 15, 'Lesson 4.5: Assignment Operators', '# Welcome to Lesson 4.5!
In this guide, you’ll learn about *assignment operators* in C, which set or update variable values. Beyond the basic =, there are shorthand operators to combine assignments with calculations, making your code concise and efficient.

---

**What Are Assignment Operators?**

*Assignment operators* assign values to variables.

They include simple assignment and compound operators that combine math or bitwise operations.

Essential for updating data during program execution.

Example: `x += 5` adds 5 to `x` and stores the result.

---

**Common Assignment Operators**

**Simple Assignment** (`=`): Sets a variable’s value.

Example: `x = 10` sets `x` to `10`.

**Add and Assign** (`+=`): Adds and updates.

Example: `x += 3` is like `x = x + 3`.

**Subtract and Assign** (`-=`): Subtracts and updates.

Example: `x -= 2` is like `x = x - 2`.

**Multiply and Assign** (`*=`): Multiplies and updates.

Example: `x *= 4` is like `x = x * 4`.

**Divide and Assign** (`/=`): Divides and updates.

Example: `x /= 2` is like `x = x / 2`.

**Modulus and Assign** (`%=`): Remainder and updates.

Example: `x %= 3` is like `x = x % 3`.

**Bitwise Operators** (e.g., `&=`, `|=`, `^=`, `<<=`, `>>=`): Combine bitwise operations.

Example: `x &= 1` is like `x = x & 1`.

---

**Using Assignment Operators**

Example Program:

```
#include <stdio.h>

int main() {
    int x = 10;
    
    printf("Initial x: %d\n", x);
    x += 5;
    printf("After += 5: %d\n", x);
    x -= 3;
    printf("After -= 3: %d\n", x);
    x *= 2;
    printf("After *= 2: %d\n", x);
    x /= 4;
    printf("After /= 4: %d\n", x);
    x %= 3;
    printf("After %%= 3: %d\n", x);
    
    return 0;
}
```


Output:

```
Initial x: 10
After += 5: 15
After -= 3: 12
After *= 2: 24
After /= 4: 6
After %= 3: 0
```


**Tip**: Compound operators save typing and are clear.

---

**Key Points**

Assignment operators (`=`, `+=`, `-=`, etc.) update variables.

Compound operators combine math or bitwise operations.

Use them to simplify code like `x = x + 1` to `x += 1`.

Ensure variable types match operations (e.g., `int` for `%`).

---

**Summary**

*Assignment operators* set or update values efficiently.

Use `=`, `+=`, `-=`, `*=` , `/=`, `%=` for quick updates.

Compound operators simplify repetitive tasks.

Happy coding!', 4, 'VIDEO', 'zv73Qv1GdwY', NULL, '2025-04-15 17:32:28.776501', 22, '2025-04-16 10:12:31.517954', 22, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (67, 19, 'Lesson 15: Two-Dimensional Arrays (Matrices) and Operations', '# Welcome to Lesson 15!
In this guide, you’ll learn about two-dimensional arrays in C, often used to represent matrices. We’ll explore how to declare and use 2D arrays, understand their structure, and perform common operations like accessing elements, initializing, and traversing. Let’s get started!

---

**What Is a Two-Dimensional Array?**

A *two-dimensional* array is an array of arrays, like a grid or table with rows and columns.

It’s used to represent matrices in math or data structures like spreadsheets.

Each element is accessed using two indices: `array[row][column]`.

**Example Concept:**

Think of a 3x2 matrix (3 rows, 2 columns) as a table:

```
10 20
30 40
50 60
```

---

**Declaring 2D Arrays**

Syntax: `type array_name[rows][columns];`

`type`: Data type (e.g., int, float).

`rows`: Number of rows.

`columns`: Number of columns.

Example:

```
int matrix[3][2]; // 3 rows, 2 columns
float table[2][4]; // 2 rows, 4 columns
```

**Note**: The size must be fixed at compile time in basic C (dynamic sizes use pointers or `malloc`).

---

**Initializing 2D Arrays**

Initialize at declaration or later using indices.

At Declaration:

```
int grid[2][3] = {
 {1, 2, 3}, // Row 0
 {4, 5, 6} // Row 1
};
// Or compactly:
int grid[2][3] = {1, 2, 3, 4, 5, 6};
```

After Declaration:

```
int matrix[2][2];
matrix[0][0] = 10;
matrix[0][1] = 20;
matrix[1][0] = 30;
matrix[1][1] = 40;
```

**Warning**: Uninitialized elements may contain random values—always initialize.

---

**Accessing 2D Array Elements**

Use two indices: `array[row][column]`.

Example:

```
#include <stdio.h>

int main() {
 int matrix[2][2] = {{1, 2}, {3, 4}};
 printf("Top-left: %d\n", matrix[0][0]); // Prints: 1
 matrix[1][1] = 10; // Change bottom-right
 printf("New bottom-right: %d\n", matrix[1][1]); // Prints: 10
 return 0;
}
```

**Caution**: Accessing invalid indices (e.g., `matrix[2][0]` in a 2x2 array) causes undefined behavior.

---

**Basic Operations: Traversing and Summing**

Use nested loops to process 2D arrays (outer loop for rows, inner for columns).

Example (Printing and Summing):

```
#include <stdio.h>

int main() {
 int matrix[2][3] = {{1, 2, 3}, {4, 5, 6}};
 int sum = 0;
 
 // Print matrix
 printf("Matrix:\n");
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 printf("%d ", matrix[i][j]);
 sum += matrix[i][j];
 }
 printf("\n");
 }
 
 printf("Sum of elements: %d\n", sum);
 return 0;
}
```

Output:
```
Matrix:
1 2 3
4 5 6
Sum of elements: 21
```

---

**Key Points**

2D arrays are grids accessed with `array[row][column]`.

Rows and columns start at index `0`.

Initialize arrays to avoid random values.

Nested loops are ideal for operations like printing or calculations.

---

**Summary**

*Two-dimensional* arrays represent matrices with rows and columns.

Declare with `type name[rows][columns]` and initialize at creation or via indices.

Use nested loops for traversal and operations like summing.

Practice with matrices to handle grid-like data confidently!

Happy coding!', 15, 'VIDEO', 'Vh4krbTnTAA', NULL, '2025-04-16 20:13:00.283979', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (68, 19, 'Lesson 16: Solving Problems Using Arrays (Linear Search & Selection Sort)', '# Welcome to Lesson 16!
In this guide, you’ll learn how to use arrays to solve common problems in C. We’ll focus on two key algorithms: Linear Search to find an element in an array and Selection Sort to sort an array. These techniques are great for beginners to understand array manipulation and problem-solving.

---

**What Are Linear Search and Selection Sort?**

**Linear Search**: A simple way to find an element by checking each array element one by one.

**Selection Sort**: A sorting method that repeatedly finds the smallest element and places it at the start of the array.

Both use arrays and are easy to implement, making them perfect for learning.

---

**Linear Search**

**How It Works**:

Start at the first element.

Compare each element with the target value.

If found, return its index; if not, indicate failure (e.g., return `-1`).

Example:

```
#include <stdio.h>

int linearSearch(int arr[], int size, int target) {
 for (int i = 0; i < size; i++) {
 if (arr[i] == target) {
 return i; // Found at index i
 }
 }
 return -1; // Not found
}

int main() {
 int arr[] = {5, 3, 8, 1, 9};
 int size = 5;
 int target = 8;
 
 int result = linearSearch(arr, size, target);
 if (result != -1) {
 printf("Found %d at index %d\n", target, result);
 } else {
 printf("%d not found\n", target);
 }
 return 0;
}
```

Output: `Found 8 at index 2`

**Note**: Linear search is simple but slow for large arrays (checks every element in worst case).

---

**Selection Sort**

**How It Works**:

Find the smallest element in the unsorted portion.

Swap it with the first element of the unsorted portion.

Repeat until the array is sorted.

Example:

```
#include <stdio.h>

void selectionSort(int arr[], int size) {
 for (int i = 0; i < size - 1; i++) {
 int minIndex = i; // Assume first is smallest
 for (int j = i + 1; j < size; j++) {
 if (arr[j] < arr[minIndex]) {
 minIndex = j; // Update smallest
 }
 }
 // Swap
 int temp = arr[i];
 arr[i] = arr[minIndex];
 arr[minIndex] = temp;
 }
}

int main() {
 int arr[] = {64, 34, 25, 12, 22};
 int size = 5;
 
 printf("Before sorting: ");
 for (int i = 0; i < size; i++) {
 printf("%d ", arr[i]);
 }
 printf("\n");
 
 selectionSort(arr, size);
 
 printf("After sorting: ");
 for (int i = 0; i < size; i++) {
 printf("%d ", arr[i]);
 }
 printf("\n");
 
 return 0;
}
```

Output:
```
Before sorting: 64 34 25 12 22
After sorting: 12 22 25 34 64
```

**Note**: Selection sort is easy to understand but inefficient for large arrays (compares many elements).

---

**Key Points**

**Linear Search**:

Returns the index of the target or `-1` if not found.

Works on unsorted arrays.

**Selection Sort**:

Sorts by repeatedly finding the minimum.

Modifies the array in place.

Arrays are ideal for these algorithms because they provide direct index access.

---

**Summary**

Linear Search checks each element to find a target, returning its index or failure.

Selection Sort organizes an array by repeatedly placing the smallest element first.

Both rely on arrays for storing and accessing data efficiently.

Practice these algorithms to build problem-solving skills!

Happy coding!', 16, 'VIDEO', 'YepJ7fDmyjI', NULL, '2025-04-16 20:13:36.075091', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (69, 19, 'Lesson 17: Using Structs to Organize Complex Data', '# Welcome to Lesson 17!
In this guide, you’ll learn how to use structs in C to organize complex data. A struct (short for structure) groups related variables into a single unit, making it easier to manage data like a student’s name, ID, and grades. We’ll cover how to define, declare, and use structs effectively.

---

**What Is a Struct?**

A *struct* is a user-defined data type that combines variables of different types (e.g., `int`, `char`, `float`).

It’s like a container for related data, such as a record or object.

Each variable inside a struct is called a *member*.

**Example Concept**:

Instead of separate variables for a book’s title, author, and price, a struct can store them together:

```
Book: title = "C Programming", author = "John", price = 29.99
```

---

**Defining a Struct**

Syntax:

```
struct struct_name {
 type1 member1;
 type2 member2;
 // ...
};
```

Example:

```
struct Student {
 char name[50];
 int id;
 float grade;
};
```

**Note**: The `struct` definition creates a new type but doesn’t allocate memory yet.

---

**Declaring Struct Variables**

Declare variables using the struct type:

At definition: `struct Student { ... } s1, s2;`

Separately: `struct Student s1;`

Example:
```
struct Student s1; // One student
struct Student class[10]; // Array of 10 students
```

**Tip**: Use `typedef` to simplify names:

```
typedef struct {
 char name[50];
 int id;
 float grade;
} Student;

Student s1; // No need for "struct"
```

---

**Using Structs**

Access members with the dot operator (`.`): `variable.member`

Initialize or assign values:

```
#include <stdio.h>
#include <string.h>

struct Student {
 char name[50];
 int id;
 float grade;
};

int main() {
 struct Student s1;
 
 // Assign values
 strcpy(s1.name, "Alice");
 s1.id = 101;
 s1.grade = 85.5;
 
 // Print values
 printf("Name: %s\n", s1.name);
 printf("ID: %d\n", s1.id);
 printf("Grade: %.1f\n", s1.grade);
 
 return 0;
}
```

Output:
```
Name: Alice
ID: 101
Grade: 85.5
```

**Note**: Use `strcpy` for strings (`s1.name = "Alice"` doesn’t work directly).

---

**Key Points**

Structs group related data into one type, like a custom record.

Use `.` to access members (e.g., `s1.grade`).

Initialize members explicitly to avoid random values.

Structs can be used in arrays, functions, or pointers (covered later).

---

**Summary**

*Structs* combine different data types into a single unit.

Define with `struct name { ... }` and access members using `.`.

Use structs for organized data like records or objects.

Practice creating and using structs to manage complex data cleanly!

Happy coding!', 17, 'VIDEO', 'dqa0KMSMx2w', NULL, '2025-04-16 20:14:14.694297', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (71, 20, 'Lesson 19: Input/Output Operations on Strings', '# Welcome to Lesson 19!
In this guide, you’ll learn how to handle input and output for strings in C using built-in functions. While you’ve used `printf` and `scanf` for numbers or simple text, strings (char arrays ending with `\0`) need special care for reading and writing. We’ll explore functions like `scanf`, `fgets`, and `printf` to manage strings, tackling challenges like spaces and buffer limits.

---

**Why Focus on String I/O?**

Strings aren’t single values like `int`—they’re arrays, so input/output can be tricky.

Common issues include reading spaces, avoiding overflows, and handling the null terminator (`\0`).

This builds on basic I/O to make you confident with text-based user interactions.

**Example Goal**: Read a full name like "John Doe" and display it, unlike basic scanf that stops at spaces.

---

**Outputting Strings**

Use `printf` with `%s` to print strings easily.

Example:

```
#include <stdio.h>

int main() {
 char message[] = "Welcome to C!";
 printf("Output: %s\n", message);
 return 0;
}
```

Output: `Output: Welcome to C!`

Note:` %s` prints until `\0`, so no manual looping is needed.

---

**Reading Strings with scanf**

Use `scanf` with `%s` to read a single word (stops at whitespace).

Example:

```
#include <stdio.h>

int main() {
 char word[20];
 printf("Enter a word: ");
 scanf("%s", word); // No & for arrays
 printf("You typed: %s\n", word);
 return 0;
}
```

**Limitations**:

Reads only one word (e.g., "John" from "John Doe").

Risk of overflow if input exceeds array size (e.g., `word[20]`).

---

**Reading Full Lines with fgets**
Use `fgets(str, size, stdin)` to read entire lines, including spaces.

Example:

```
#include <stdio.h>
#include <string.h>

int main() {
 char fullName[50];
 printf("Enter your full name: ");
 fgets(fullName, 50, stdin);
 
 // Remove trailing newline
 fullName[strcspn(fullName, "\n")] = ''\0'';
 
 printf("Hello, %s!\n", fullName);
 return 0;
}
```

**Advantages**:

Handles spaces (e.g., "John Doe").

Limits input to `size` to prevent overflow.

Caution: `fgets` includes `\n` if there’s room—remove it for clean strings.

**Avoid**: `gets` is unsafe (no size limit) and outdated—never use it.

---

**Practical Example: Combining I/O**

Read a word and a sentence, then display them.

Example:

```
#include <stdio.h>
#include <string.h>

int main() {
 char city[20], desc[50];
 
 printf("Enter a city: ");
 scanf("%s", city);
 
 // Clear buffer before fgets
 while (getchar() != ''\n'');
 
 printf("Describe it: ");
 fgets(desc, 50, stdin);
 desc[strcspn(desc, "\n")] = ''\0'';
 
 printf("You said: %s is %s\n", city, desc);
 return 0;
}
```

Output (example input):

```
Enter a city: Paris
Describe it: beautiful and vibrant
You said: Paris is beautiful and vibrant
```

**Why Clear Buffer**?: `scanf` leaves newlines, which `fgets` might grab.

---

**Key Points**

Use `printf` with `%s` to display strings cleanly.

`scanf` grabs one word; `fgets` gets full lines safely.

Always set array sizes (e.g., `char str[50]`) to avoid crashes.

Handle `fgets` newlines with `strcspn` or similar.

---

**Summary**

*String output* uses `printf` and `%s` for quick display.

*String input* needs `scanf` for words or `fgets` for lines.

Manage sizes and newlines to keep strings safe and clean.

Practice I/O to create user-friendly text programs!

Happy coding!', 19, 'VIDEO', '2HasEQe5VR0', NULL, '2025-04-16 20:15:15.010749', 22, '2025-04-16 20:15:39.817236', 22, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (70, 20, 'Lesson 18: Declaring and Initializing Strings', '# Welcome to Lesson 18!
In this guide, you’ll learn how to declare, initialize, and manipulate strings in C. A string is a sequence of characters used to store text, like names or messages. We’ll cover the basics of working with strings and introduce useful functions from the `string.h` library.

---

**What Is a String in C?**

A string is an array of `char` ending with a null character (`\0`).

The null character marks the end of the string, telling C where it stops.

Example: The string `"Hello"` is stored as `{''H'', ''e'', ''l'', ''l'', ''o'', ''\0''}`.

**Why It Matters**: Strings let you handle text, but they’re trickier in C than in other languages—no built-in string type!

---

**Declaring Strings**

Declare a string as a `char` array or a `char` pointer.

Syntax:

Array: `char string_name[size];`

Pointer: `char *string_name;`

Example:

```
char name[20]; // Array for up to 19 chars + \0
char *greeting; // Pointer, needs initialization
```

**Note**: Always ensure the array size is large enough for the string plus `\0`.

---

**Initializing Strings**

Initialize at declaration or later.

At Declaration:

```
char city[10] = "Paris"; // Includes \0 automatically
char message[] = "Hello"; // Size set to 6 (5 chars + \0)
```

After Declaration:

```
char name[20];
strcpy(name, "Alice"); // Use strcpy from string.h
```

**Warning**: You can’t assign strings directly after declaration:

```
char text[10];
text = "Error"; // WRONG
strcpy(text, "Correct"); // RIGHT
```

---

**Manipulating Strings**

Use functions from `<string.h>` for common tasks:

`strcpy(dest, src)`: Copy a string.

`strlen(str)`: Get string length (excludes `\0`).

`strcmp(str1, str2)`: Compare strings (returns 0 if equal).

`strcat(dest, src)`: Concatenate (append) strings.

Example:

```
#include <stdio.h>
#include <string.h>

int main() {
 char str1[20] = "Hello";
 char str2[20];
 
 // Copy
 strcpy(str2, str1);
 printf("Copied: %s\n", str2);
 
 // Length
 printf("Length of str1: %zu\n", strlen(str1));
 
 // Concatenate
 strcat(str1, ", World!");
 printf("Concatenated: %s\n", str1);
 
 // Compare
 if (strcmp(str1, str2) == 0) {
 printf("Strings are equal\n");
 } else {
 printf("Strings differ\n");
 }
 
 return 0;
}
```

Output:
```
Copied: Hello
Length of str1: 5
Concatenated: Hello, World!
Strings differ
```

**Tip**: Always include `<string.h>` for these functions.

---

**Key Points**

Strings are `char` arrays ending with `\0`.

Declare with enough space for characters plus the null terminator.

Use `strcpy`, not `=`, for string assignment after declaration.

Functions like `strlen` and `strcat` make string tasks easier.

---

**Summary**

Strings are character arrays with a `\0` at the end.

Declare with `char name[size]` or initialize with `"text"`.

Manipulate using `<string.h>` functions like `strcpy` and `strcmp`.

Practice string operations to handle text confidently!

Happy coding!', 18, 'VIDEO', 'cnfRyvo41Bs', NULL, '2025-04-16 20:14:41.895784', 22, '2025-04-16 20:15:28.422105', 22, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (72, 20, 'Lesson 20: Array of Strings and String Operations (string.h)', '# Welcome to Lesson 20!
In this guide, you’ll learn how to store and manipulate multiple strings in C using arrays of strings. We’ll also dive into powerful functions from the `<string.h>` library to perform operations like copying, comparing, and concatenating strings. This builds on your string knowledge to handle lists of text efficiently.

---

**What Is an Array of Strings?**

An *array of strings* is a collection of strings, like a list of names or words.

In C, it’s typically a *two-dimensional* char array or an array of char pointers.

Each string is a `char` array ending with `\0`, and the array groups multiple such strings.

Example Concept:
Think of storing a list of fruits: `{"Apple", "Banana", "Orange"}`.

---

**Declaring an Array of Strings**

Two common ways:

**2D Char Array**: `char array_name[size][length];`

`size`: Number of strings.

`length`: Max length of each string (including \0).

**Array of Pointers**: `char *array_name[size];`

Examples:
```
char colors[3][10] = {"Red", "Blue", "Green"}; // 2D array
char *fruits[3] = {"Apple", "Banana", "Orange"}; // Pointer array
```

**Note**: The 2D array needs a fixed length for all strings; pointers are more flexible but require careful memory management.

---

**Initializing an Array of Strings**

Initialize at declaration:

```
char days[4][10] = {
 "Monday",
 "Tuesday",
 "Wednesday",
 "Thursday"
};
// Or compactly:
char days[4][10] = {"Monday", "Tuesday", "Wednesday", "Thursday"};
```

With pointers:
```
char *planets[3] = {"Mars", "Venus", "Earth"};
```

**Tip**: Ensure enough space in 2D arrays (e.g., `10` for strings up to 9 chars + `\0`).

---

**Manipulating Strings with string.h**

The `<string.h>` library provides functions to work with strings:

`strcpy(dest, src)`: Copy `src` to `dest`.

`strcat(dest, src)`: Append `src` to `dest`.

`strcmp(str1, str2)`: Compare strings (returns 0 if equal, negative if `str1 < str2`, positive if `str1 > str2`).

`strlen(str)`: Return string length (excludes `\0`).

Example (Using an Array of Strings):

```
#include <stdio.h>
#include <string.h>

int main() {
 char names[3][20] = {"Alice", "Bob", "Charlie"};
 
 // Print all names
 printf("Names:\n");
 for (int i = 0; i < 3; i++) {
 printf("%s (length: %zu)\n", names[i], strlen(names[i]));
 }
 
 // Compare two strings
 if (strcmp(names[0], names[1]) < 0) {
 printf("%s comes before %s\n", names[0], names[1]);
 }
 
 // Concatenate
 char result[40];
 strcpy(result, names[0]);
 strcat(result, " and ");
 strcat(result, names[1]);
 printf("Combined: %s\n", result);
 
 return 0;
}
```

Output:
```
Names:
Alice (length: 5)
Bob (length: 3)
Charlie (length: 7)
Alice comes before Bob
Combined: Alice and Bob
```

**Caution**: Ensure `dest` has enough space for `strcpy` or `strcat` to avoid overflow.

---

**Key Points**

Arrays of strings can be 2D char arrays (`char[][]`) or pointer arrays (`char*[]`).

Use `<string.h>` functions for safe string operations.

Always account for `\0` when setting string sizes.

Pointer arrays are flexible but need careful handling (e.g., no direct string reassignment).

---

**Summary**

*Arrays of strings* store multiple strings as 2D arrays or pointer arrays.

Declare with `char array[size][length]` or `char *array[size]`.

Use `<string.h>` for tasks like copying (`strcpy`), joining (`strcat`), and comparing (`strcmp`).

Practice string arrays to manage lists of text like a pro!

Happy coding!', 20, 'VIDEO', 'e7SACGE9hKw', NULL, '2025-04-16 20:16:10.129022', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (45, 14, 'Lesson 1: Variables and Constants in C', '# Welcome to Lesson 1! 
In this guide, you’ll learn about variables and constants in C, the building blocks of any program. Variables let you store and change data, while constants keep values fixed. We’ll explore how to declare, initialize, and use them to start coding in C.

---

**What Are Variables and Constants?**

**Variables:**

Named storage for data that can change during program execution.

Example: A variable `score` might hold `90` now and `85` later.

**Constants:**

Named values that cannot change once set.

Example: A constant `PI` stays `3.14` throughout the program.

**Why They Matter**: Variables and constants organize data, making programs flexible and readable.

---

**Declaring Variables**
Syntax: `type variable_name;`

`type`: Data type (e.g., `int`, `float`, `char`).

`variable_name`: A unique name (e.g., `age`, `price`).

Examples:
```
int count;    // Integer variable
float temp;   // Floating-point variable
char letter;  // Character variable
```

**Note**: Choose meaningful names (e.g., `total` instead of `x`) for clarity.

---

**Initializing Variables**

Assign a value when declaring or later.

Examples:
```
int age = 25;       // Declare and initialize
float salary;       // Declare only
salary = 50000.75;  // Initialize later
```

**Warning**: Uninitialized variables may hold random values—always set them before use.

---

**Declaring Constants**

Use the `const` keyword or `#define` to create constants.

Syntax:

`const type constant_name = value;`

`#define CONSTANT_NAME value`

Examples:

```
const float PI = 3.14; // Using const
#define MAX_SCORE 100  // Using #define
```

**Note**: Constants can’t be changed:

```
const int LIMIT = 10;
LIMIT = 20; // Error!
```

---

**Using Variables and Constants**

Example Program:

```
#include <stdio.h>
#define TAX_RATE 0.1

int main() {
    int price = 200;
    const int DISCOUNT = 50;
    float total;
    
    total = (price - DISCOUNT) * (1 + TAX_RATE);
    
    printf("Original price: %d\n", price);
    printf("Discount: %d\n", DISCOUNT);
    printf("Total with tax: %.2f\n", total);
    
    return 0;
}
```

Output:
```
Original price: 200
Discount: 50
Total with tax: 165.00
```

**Explanation**:
`price` is a variable that can change.
`DISCOUNT` and `TAX_RATE` are constants, fixed for the program.
`total` calculates the final cost using both.

---

**Key Points**

Variables store changeable data; constants hold fixed values.

Declare variables with `type name` and initialize to avoid random values.

Use `const` or `#define` for constants to ensure they don’t change.

Meaningful names improve code readability.

---

**Summary**

*Variables* let you store and update data like numbers or letters.

*Constants* keep values like `PI` or limits fixed.

Declare with proper types and initialize for safety.

Start practicing to make data handling second nature!

Happy coding!', 1, 'VIDEO', 'videos/1744809551480_TestVideo188.mp4', 'lessons/13ca0b7f-7943-45ca-9b50-594bb092ad8e-MetamaskExtensionURL.txt', '2025-04-15 16:21:20.834272', 22, '2025-04-16 20:19:08.526057', 22, 'ACTIVATED');


--
-- Data for Name: lesson_problem; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- Data for Name: top_course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.top_course OVERRIDING SYSTEM VALUE VALUES (3032, 8, 5);
INSERT INTO schema_course.top_course OVERRIDING SYSTEM VALUE VALUES (3033, 1, 4);
INSERT INTO schema_course.top_course OVERRIDING SYSTEM VALUE VALUES (3034, 5, 3);
INSERT INTO schema_course.top_course OVERRIDING SYSTEM VALUE VALUES (3035, 2, 2);
INSERT INTO schema_course.top_course OVERRIDING SYSTEM VALUE VALUES (3036, 9, 1);
INSERT INTO schema_course.top_course OVERRIDING SYSTEM VALUE VALUES (3037, 4, 0);


--
-- Data for Name: user_lesson_progress; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.user_lesson_progress VALUES (1, 5);
INSERT INTO schema_course.user_lesson_progress VALUES (136, 35);
INSERT INTO schema_course.user_lesson_progress VALUES (136, 37);
INSERT INTO schema_course.user_lesson_progress VALUES (136, 36);


--
-- Name: chapter_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.chapter_id_seq', 20, true);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_id_seq', 50, true);


--
-- Name: course_rating_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_rating_id_seq', 6, true);


--
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.lesson_id_seq', 72, true);


--
-- Name: top_course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.top_course_id_seq', 3037, true);


--
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (id);


--
-- Name: course_comment course_comment_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_pkey PRIMARY KEY (course_id, comment_id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: course_rating course_rating_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_rating
    ADD CONSTRAINT course_rating_pkey PRIMARY KEY (id);


--
-- Name: course_topic course_topic_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT course_topic_pkey PRIMARY KEY (course_id, topic_id);


--
-- Name: course_user course_user_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_pkey PRIMARY KEY (course_id, user_id);


--
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- Name: lesson_problem lesson_problem_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_pkey PRIMARY KEY (lesson_id, problem_id);


--
-- Name: user_lesson_progress user_lesson_progress_pkey; Type: CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.user_lesson_progress
    ADD CONSTRAINT user_lesson_progress_pkey PRIMARY KEY (user_id, lesson_id);


--
-- Name: chapter chapter_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- Name: chapter chapter_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- Name: chapter chapter_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.chapter
    ADD CONSTRAINT chapter_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- Name: course_comment course_comment_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT course_comment_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- Name: course course_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- Name: course_rating course_rating_course_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_rating
    ADD CONSTRAINT course_rating_course_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: course_rating course_rating_user_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_rating
    ADD CONSTRAINT course_rating_user_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: course course_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course
    ADD CONSTRAINT course_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- Name: course_user course_user_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON DELETE CASCADE;


--
-- Name: course_user course_user_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_user
    ADD CONSTRAINT course_user_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON DELETE CASCADE;


--
-- Name: course_topic fk_course; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES schema_course.course(id) ON DELETE CASCADE;


--
-- Name: course_topic fk_topic; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_topic
    ADD CONSTRAINT fk_topic FOREIGN KEY (topic_id) REFERENCES schema_setting.topic(id) ON DELETE CASCADE;


--
-- Name: course_comment fkk241lpfgmhec5lpxv4x6qod9; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.course_comment
    ADD CONSTRAINT fkk241lpfgmhec5lpxv4x6qod9 FOREIGN KEY (comment_id) REFERENCES schema_discussion.comment(id);


--
-- Name: lesson lesson_chapter_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES schema_course.chapter(id);


--
-- Name: lesson lesson_created_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_created_by_fkey FOREIGN KEY (created_by) REFERENCES schema_user.users(id);


--
-- Name: lesson_problem lesson_problem_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id);


--
-- Name: lesson_problem lesson_problem_problem_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson_problem
    ADD CONSTRAINT lesson_problem_problem_id_fkey FOREIGN KEY (problem_id) REFERENCES schema_problem.problem(id);


--
-- Name: lesson lesson_updated_by_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.lesson
    ADD CONSTRAINT lesson_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES schema_user.users(id);


--
-- Name: top_course top_course_course_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.top_course
    ADD CONSTRAINT top_course_course_id_fkey FOREIGN KEY (course_id) REFERENCES schema_course.course(id);


--
-- Name: user_lesson_progress user_lesson_progress_lesson_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.user_lesson_progress
    ADD CONSTRAINT user_lesson_progress_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES schema_course.lesson(id) ON DELETE CASCADE;


--
-- Name: user_lesson_progress user_lesson_progress_user_id_fkey; Type: FK CONSTRAINT; Schema: schema_course; Owner: postgres
--

ALTER TABLE ONLY schema_course.user_lesson_progress
    ADD CONSTRAINT user_lesson_progress_user_id_fkey FOREIGN KEY (user_id) REFERENCES schema_user.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

