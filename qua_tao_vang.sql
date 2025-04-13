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

SET default_tablespace = kodeholik_course_data;

SET default_table_access_method = heap;

--
-- Name: chapter; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
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
-- Name: course; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
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
-- Name: course_comment; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.course_comment (
    course_id bigint NOT NULL,
    comment_id bigint NOT NULL
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


SET default_tablespace = '';

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
    finished boolean DEFAULT false
);


ALTER TABLE schema_course.course_user OWNER TO postgres;

SET default_tablespace = kodeholik_course_data;

--
-- Name: lesson; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
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
-- Name: lesson_problem; Type: TABLE; Schema: schema_course; Owner: postgres; Tablespace: kodeholik_course_data
--

CREATE TABLE schema_course.lesson_problem (
    lesson_id bigint NOT NULL,
    problem_id bigint NOT NULL
);


ALTER TABLE schema_course.lesson_problem OWNER TO postgres;

SET default_tablespace = '';

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

INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (6, 16, 'Chapter 1: Variables, Constants, and Data Types', 'This chapter introduces variables, constants, and data types in C. Students will learn how to declare and initialize variables, understand memory operations, and use basic input/output functions.', 1, 'ACTIVATED', '2025-03-25 14:14:50.626098', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (7, 16, 'Chapter 2: Expressions and Operators', 'This chapter covers different types of expressions and operators in C, including arithmetic, logical, and bitwise operations.

', 2, 'ACTIVATED', '2025-03-25 14:15:23.155517', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (8, 16, 'Chapter 3: Control Structures', 'Learn how to control the flow of a program using selection and loop structures.', 3, 'ACTIVATED', '2025-03-25 14:15:51.127157', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (9, 16, 'Chapter 4: Functions and Modular Programming', 'Understand how to write modular programs using functions in C.', 4, 'ACTIVATED', '2025-03-25 14:16:24.297647', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (11, 16, 'Chapter 5: Pointers in C', 'Learn how pointers work and how to use them for efficient memory manipulation.', 5, 'ACTIVATED', '2025-04-13 19:32:11.966655', 22, '2025-04-13 19:33:25.800401', 22);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (12, 16, 'Chapter 6: Arrays and Structures', 'Work with arrays and structures to store and manage data effectively.', 6, 'ACTIVATED', '2025-04-13 19:34:44.091016', 22, NULL, NULL);
INSERT INTO schema_course.chapter OVERRIDING SYSTEM VALUE VALUES (13, 16, 'Chapter 7: String Handling in C', 'Understand how to work with strings and perform string operations.', 7, 'ACTIVATED', '2025-04-13 19:35:29.768727', 22, NULL, NULL);


--
-- Data for Name: course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course OVERRIDING SYSTEM VALUE VALUES (16, 'PRF192 - Programming Fundamentals', '## Course Description

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
- Enhance individual and teamwork behaviors.', 'kodeholik-course-image-5689c949-e19f-4a57-9375-af5e8ee4a555', 'ACTIVATED', '2025-03-20 14:21:57.559', 22, '2025-04-13 22:19:33.873', 22, 0, 2);


--
-- Data for Name: course_comment; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- Data for Name: course_rating; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- Data for Name: course_topic; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_topic VALUES (16, 18);
INSERT INTO schema_course.course_topic VALUES (16, 9);
INSERT INTO schema_course.course_topic VALUES (16, 3);
INSERT INTO schema_course.course_topic VALUES (16, 2);
INSERT INTO schema_course.course_topic VALUES (16, 6);


--
-- Data for Name: course_user; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.course_user VALUES (16, 3, '2025-04-10 08:52:29.619', 0, NULL, NULL, 0, false);
INSERT INTO schema_course.course_user VALUES (16, 22, '2025-04-13 20:48:35.743', 0, '2025-04-13 22:51:38.174111', '2025-04-13 22:51:32.582393', 59, false);


--
-- Data for Name: lesson; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (37, 6, 'Lesson 1: Variables and Constants in C', '- Learn how to declare and initialize variables and constants.
- Understand the differences between variables and constants.
- Practice: Declare and use variables/constants in a simple program.', 1, 'VIDEO', 'aIQk1O08zpg', NULL, '2025-03-25 14:18:02.614776', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (38, 6, 'Lesson 2: Understanding Data Types', '- Explore different data types in C (int, float, char, etc.).
- Learn the importance of choosing the right data type.

', 1, 'DOCUMENT', NULL, 'lessons/bbf6c0f8-2647-4f01-b5e3-ac1ee54bc75e-Data Type In C.txt', '2025-03-25 14:24:23.659063', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (39, 6, 'Lesson 3: Basic Memory Operations & Input/Output Functions (scanf, printf)', '- Understand how memory is allocated for variables.
- Learn to use `scanf` and `printf` for user input and output.

', 1, 'VIDEO', 'xOIVXR35aI4', NULL, '2025-03-25 14:27:46.789062', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (40, 7, 'Lesson 4: Arithmetic, Relational, Logical, Bitwise, and Assignment Operators', '- Learn how different operators work in C.
- Understand when and how to use relational and logical operators.

', 1, 'VIDEO', '5JXcX0IqRUo', NULL, '2025-03-25 14:34:13.674859', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (41, 7, 'Lesson 5: Mixing Data Types, Casting, and Operator Precedence', '- Learn how implicit and explicit type conversion works.
- Understand operator precedence in expressions.', 1, 'DOCUMENT', NULL, 'lessons/9c3a3b08-bfee-4e5d-8f63-d31f55aff919-Mixing Data Types, Casting, and Operator Precedence.txt', '2025-03-25 14:38:34.991167', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (43, 8, 'Lesson 6.2: Selection Structures (switch-case)', '- Understand conditional statements in C.
- Learn when to use `if-else` vs. `switch-case`.

', 1, 'VIDEO', 'tjd8fQw5HTA', NULL, '2025-03-25 14:40:37.377564', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (42, 8, 'Lesson 6.1: Selection Structures (if-else)', '- Understand conditional statements in C.
- Learn when to use `if-else` vs. `switch-case`.

', 1, 'VIDEO', 'xyQrVEFz9kw', NULL, '2025-03-25 14:40:32.519989', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (45, 8, 'Lesson 7.2: Loop Structures (while)', '- Learn how looping mechanisms work in C.
- Understand the difference between `for`, `while`, and `do-while` loops.', 1, 'VIDEO', 'ufFAFx5Qn3w', NULL, '2025-03-25 14:44:47.605039', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (46, 8, 'Lesson 7.3: Loop Structures (do-while)', '- Learn how looping mechanisms work in C.
- Understand the difference between `for`, `while`, and `do-while` loops.', 1, 'VIDEO', 'BIiE6LNy6Ck', NULL, '2025-03-25 14:45:05.403441', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (44, 8, 'Lesson 7.1: Loop Structures (for)', '- Learn how looping mechanisms work in C.
- Understand the difference between `for`, `while`, and `do-while` loops.', 1, 'VIDEO', 'b4DPj0XAfSg', NULL, '2025-03-25 14:44:20.54632', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (47, 9, 'Lesson 8: Function Definitions', '- Learn how to define functions in C.
- Understand function prototypes and implementation.', 1, 'DOCUMENT', NULL, 'lessons/2fcf4b74-2496-4de3-917d-70bdbfad2a02-Function Definitions.txt', '2025-03-25 14:47:49.486412', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (48, 9, 'Lesson 9: Using Functions and Pass by Value', '- Learn about function parameters and argument passing.
- Understand pass-by-value behavior in functions.

', 1, 'DOCUMENT', NULL, 'lessons/d5bc8605-10c0-4290-9df3-eb45dcdde488-Function Definitions.txt', '2025-03-25 14:49:10.166224', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (49, 9, 'Lesson 10: Scope and Lifetime of Variables', '- Understand the concepts of local and global variables.
- Learn about variable lifetime and scope.', 1, 'DOCUMENT', NULL, 'lessons/3a5da3ca-f6ec-4ee3-b50a-713f81946626-SCOPE AND LIFETIME OF VARIABLES .txt', '2025-03-25 14:51:08.507673', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (52, 11, 'Lesson 11: Pointer Declarations and Operators', '# Welcome to Lesson 11! 
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

Happy coding!', 11, 'VIDEO', 'DplxIq0mc_Y', NULL, '2025-04-13 19:57:26.157991', 22, '2025-04-13 20:00:06.031318', 22, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (54, 11, 'Lesson 13: Dynamically Allocated Memory', '# Welcome to Lesson 13! 
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

Happy coding!', 13, 'VIDEO', 'R0qIYWo8igs', NULL, '2025-04-13 20:19:22.213599', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (55, 12, 'Lesson 14: One-Dimensional Arrays and Basic Operations', '# Welcome to Lesson 14!
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

Happy coding!', 14, 'VIDEO', 'eE9MnoS0lc0', NULL, '2025-04-13 20:26:21.360752', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (56, 12, 'Lesson 15: Two-Dimensional Arrays (Matrices) and Operations', '# Welcome to Lesson 15!
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

Happy coding!', 15, 'VIDEO', 'Vh4krbTnTAA', NULL, '2025-04-13 20:32:41.568223', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (57, 12, 'Lesson 16: Solving Problems Using Arrays (Linear Search & Selection Sort)', '# Welcome to Lesson 16!
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

Happy coding!', 16, 'VIDEO', 'YepJ7fDmyjI', NULL, '2025-04-13 20:40:46.085029', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (58, 12, 'Lesson 17: Using Structs to Organize Complex Data', '# Welcome to Lesson 17!
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

Happy coding!', 17, 'VIDEO', 'dqa0KMSMx2w', NULL, '2025-04-13 20:46:58.042858', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (59, 13, 'Lesson 18: Declaring and Initializing Strings', '# Welcome to Lesson 18!
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

Happy coding!', 18, 'VIDEO', 'cnfRyvo41Bs', NULL, '2025-04-13 20:56:35.038733', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (60, 13, 'Lesson 19: Input/Output Operations on Strings', '# Welcome to Lesson 19!
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

Happy coding!', 19, 'VIDEO', '2HasEQe5VR0', NULL, '2025-04-13 21:08:58.442751', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (61, 13, 'Lesson 20: Array of Strings and String Operations (string.h)', '# Welcome to Lesson 20!
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

Happy coding!', 20, 'VIDEO', 'e7SACGE9hKw', NULL, '2025-04-13 21:45:48.388925', 22, NULL, NULL, 'ACTIVATED');
INSERT INTO schema_course.lesson OVERRIDING SYSTEM VALUE VALUES (53, 11, 'Lesson 12: Passing Pointers to Functions (Pass by Reference)', '# Welcome to Lesson 12!

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

Happy coding!', 12, 'VIDEO', 'DplxIq0mc_Y', NULL, '2025-04-13 20:09:10.703', 22, '2025-04-13 21:50:40.186674', 22, 'ACTIVATED');


--
-- Data for Name: lesson_problem; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- Data for Name: top_course; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--



--
-- Data for Name: user_lesson_progress; Type: TABLE DATA; Schema: schema_course; Owner: postgres
--

INSERT INTO schema_course.user_lesson_progress VALUES (3, 37);
INSERT INTO schema_course.user_lesson_progress VALUES (22, 53);
INSERT INTO schema_course.user_lesson_progress VALUES (22, 37);


--
-- Name: chapter_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.chapter_id_seq', 13, true);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_id_seq', 19, true);


--
-- Name: course_rating_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.course_rating_id_seq', 6, true);


--
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.lesson_id_seq', 61, true);


--
-- Name: top_course_id_seq; Type: SEQUENCE SET; Schema: schema_course; Owner: postgres
--

SELECT pg_catalog.setval('schema_course.top_course_id_seq', 1167, true);


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

