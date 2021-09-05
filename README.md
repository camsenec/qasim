# Coloring Problem with Quantum Annealing
## Overview
This application solves coloring problem with quantum annealing machine emulator

## How to Use
1. Making Map to be colored  
First, select the number of fractal generation repeats from the pull-down below the `CREATE FIELD` button.
After that, press the `CREATE FIELD` button to create a fractal map to be colored.
After the shape is created, press the `CREATE MODEL` button to proceed to quantum computer configuration
   
2. Configuration quantum annealing machine
Specify the name of the model in the first field. The name of the model can be anything.
Next, specify the number of slices in the second field.
The number of slices, also known as the Trotter number, refers to how many quantum state are given.
If you specify 10 for the number of slices, 10 quantum state will be given.
Increasing the number of slices theoretically improves the optimality, but require more time. Recommended number is 10.
After entering the values in the two fields and pressing the `REGISTER` button, the created figure in step 1 is converted to a spin glass model which can be processed with configured quantum annealing machine.

3. Visualizing Result
When the processing in the simulator is completed, the screen changes.
Click the `SHOW RESULT` button to display the result of coloring the created figure.
At this time, the accuracy rate will be displayed in a pop-up.

## Program Structure

      com.tanakatomoya.qasimulator--+Activity(Class for activity. It is shown in section of Activity Transition
                                    |                                    
                                    +--DrawableObject(Objects drawn on View/FieldView)
                                    |
                                    +--IO (FileIO)
                                    |
                                    +--Model (SpinGlassFieldModel)
                                    |
                                    +--Retrofit (API Interface)
                                    |
                                    +--Thread (Thread for Download and Upload. These are used in CreateModelActivity)
                                    |
                                    +--View (FieldView is contained. It is used in CreateFieldActivity and ResultActivity)


## Programing language
- [FrontEnd] Java  
- [Server Side] Python
- [Solver]  Fortran2003

## Other git repository
- [Server Side] https://github.com/camsenec/qasim-server
- [Solver] https://github.com/camsenec/qasim-solver
- [Emulator] https://github.com/camsenec/qa-emulator

## Author
Tomoya Tanaka
