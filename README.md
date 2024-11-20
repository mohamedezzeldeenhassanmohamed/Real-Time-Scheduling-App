# Real-Time-Scheduling-App

Real-time operating systems (RTOS) 

are designed to handle tasks that require precise timing and high reliability. Here are some common scheduling algorithms used in RTOS:

1. **Rate Monotonic Scheduling (RMS)**: This is a fixed-priority algorithm where tasks are assigned priorities based on their periodicity. The shorter the period, the higher the priority.

2. **Earliest Deadline First (EDF)**: This dynamic priority algorithm assigns priorities based on the deadlines of tasks. The task with the closest deadline gets the highest priority.

3. **Least Laxity First (LLF)**: This algorithm assigns priorities based on the laxity (slack time) of tasks. Laxity is the difference between the deadline and the remaining execution time. Tasks with the least laxity are given the highest priority.

4. **Fixed Priority Pre-emptive Scheduling (FPPS)**: In this algorithm, tasks are assigned fixed priorities, and the scheduler pre-empts lower-priority tasks to execute higher-priority ones.

5. **Round Robin Scheduling**: This is a time-sharing algorithm where each task is assigned a fixed time slice in a cyclic order. It is simple but not always suitable for real-time systems due to its lack of prioritization.

These algorithms help ensure that critical tasks meet their deadlines, which is essential for the proper functioning of real-time systems.

: [University of Texas at Arlington](https://crystal.uta.edu/~kumar/cse6306/papers/RealTime_Vimal.pdf)
: [University of Connecticut](https://cps.cse.uconn.edu/wp-content/uploads/sites/2687/2019/10/ch6.2.pdf)
: [Dextutor](https://dextutor.com/real-time-scheduling-algorithms/)


- **Android** 


- **Kotlin**



<div align=center>
        <img src="https://user-images.githubusercontent.com/81251707/214865310-4b59aeeb-080f-4f4e-8697-d348f033d26b.jpg" height="400" width="200">
</div>
</p>
</p>


  - Static table-driven



<div align=center>
        <img src="https://user-images.githubusercontent.com/81251707/214865382-b395f7dd-a620-4515-8dd2-c0adc5ad8d1e.jpg" height="400" width="200">
        <img src="https://user-images.githubusercontent.com/81251707/214865386-b2db5e77-6dde-455e-9686-3c9c8b17dbf9.jpg" height="400" width="200">
        <img src="https://user-images.githubusercontent.com/81251707/214865391-3827090d-4ba8-44c5-bdcb-bfe3ece0104f.jpg" height="400" width="200">
</div>
</p>
</p>


  - Static priority-driven preemptive



<div align=center>
        <img src="https://user-images.githubusercontent.com/81251707/214865398-174e0b4e-a3d8-419c-842a-e6cb3ea5781e.jpg" height="400" width="200">
        <img src="https://user-images.githubusercontent.com/81251707/214865403-b75687a7-09ac-44b5-99d5-7cf95df65e7e.jpg" height="400" width="200">
        <img src="https://user-images.githubusercontent.com/81251707/214865407-30023352-3ee4-4715-b756-8bc0eaa9147f.jpg" height="400" width="200">
</div>
