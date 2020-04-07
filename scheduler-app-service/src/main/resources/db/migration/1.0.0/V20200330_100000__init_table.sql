-- Schema
CREATE SCHEMA IF NOT EXISTS scheduler;

-- Tables
CREATE TABLE IF NOT EXISTS scheduler.task
(
    id          UUID                        NOT NULL
        CONSTRAINT task_pkey PRIMARY KEY,
    due_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    resolved_at TIMESTAMP WITHOUT TIME ZONE,
    title       VARCHAR                     NOT NULL,
    description VARCHAR                     NOT NULL,
    priority    INTEGER                     NOT NULL,
    status      VARCHAR                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE
);

-- Indexes
CREATE INDEX status_idx ON scheduler.task (status);
CREATE INDEX due_date_idx ON scheduler.task (due_date);

/*-- Comments
COMMENT ON SCHEMA scheduler IS 'This schema holds all Scheduled Task related data';

COMMENT ON TYPE TaskStatus IS 'This Enum holds all the possible value of TaskStatus';

COMMENT ON TABLE scheduler.task IS 'Stores Task related data';
COMMENT ON COLUMN scheduler.task.id IS 'Id of the record';
COMMENT ON COLUMN scheduler.task.due_date IS 'Due Date of the Task';
COMMENT ON COLUMN scheduler.task.resolved_at IS 'Task Resolved at';
COMMENT ON COLUMN scheduler.task.title IS 'Title of the Task';
COMMENT ON COLUMN scheduler.task.description IS 'description of the Task';
COMMENT ON COLUMN scheduler.task.priority IS 'priority of the Task';
COMMENT ON COLUMN scheduler.task.status IS 'status of the Task';
COMMENT ON COLUMN scheduler.task.created_at IS 'The date on which the Task created';
COMMENT ON COLUMN scheduler.task.updated_at IS 'The date on which the Task updated';

COMMENT ON CONSTRAINT task_pkey ON scheduler.task IS 'Primary key of the Task';
COMMENT ON INDEX scheduler.status_idx IS 'Index on Field status';
COMMENT ON INDEX scheduler.due_date_idx IS 'Index on Field dueDate';
*/