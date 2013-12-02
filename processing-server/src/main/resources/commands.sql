--Übersicht offener Tasks pro laufender Prozess-Instanz
select
task.id_ AS TASK_ID,
task.name_ AS TASK_NAME,
task.create_time_ AS TASK_CREATION_DATE,
task.owner_ AS TASK_OWNER,
task.assignee_ AS TASK_ASSIGNEE,
process_inst.id_ AS PROC_INST_ID,
process_def.id_ PROC_DEF_ID,
process_def.name_ PROC_DEF_NAME,
process_def.key_ as PROC_DEF_KEY
from act_ru_task as task
inner join act_ru_execution as process_inst on (process_inst.id_ = task.execution_id_)
inner join act_re_procdef process_def on (process_def.id_ = process_inst.proc_def_id_)
order by process_inst.id_ asc

---

--Übersicht Prozess-Variablen pro laufender Prozess-Instanz
select
process_inst.id_ AS PROC_INST_ID,
variable.name_ as VARIABLE_NAME,
variable.long_ as VARIABLE_NUMERIC_VALUE,
variable.text_ as VARIABLE_TEXT_VALUE,
variable.type_ as VARIABLE_TYPE
from act_ru_variable as variable
inner join act_ru_execution as process_inst on (variable.execution_id_ = process_inst.id_)
order by process_inst.id_ asc, variable.name_ asc