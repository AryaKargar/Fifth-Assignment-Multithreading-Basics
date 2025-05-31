# Question 01
### the output would be like this:

`Calling run()
Running in: main
Calling start()
Running in: Thread-2`

### *Because :* 
when you use the run method it would work just like a normal method,
and it will be called from the **Main** thread but the start method would call the run method in a new 
thread named **Thread-2** .

So calling run doesn't use the Thread logics and isn't concurrent neither real multithread .


# Question 02
### the output would be like this:

`Main thread ends. Daemon thread running... [it can be diffrent number of lines]
`
### *this happens because*
Once the main thread finishes, the JVM checks if only daemon threads remain. If yes, it terminates the process, potentially stopping daemon threads before they finish.

If thread.setDaemon(true) gets removed the thread would end its job before being terminated

Garbage Collector , Auto-Save Timers in GUI Apps and  File Watchers are some real life use cases of daemon threads



# Question 03
### the output would be like this:

`Thread is running using a ...!
`

`() -> { ... }` is called Lambda Expression.

- it is a shorter version of implementing runnable interface or extending Thread
- Lambda is better for short tasks
- can't extend other classes





