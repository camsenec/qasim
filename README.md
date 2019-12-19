# Quantum Annealing Simulator
## Overview
This application solves coloring problem with quantum annealing machine emulator

## How to Use
1. 彩色する図形の作成   
まず, 「CREATE FIELD」ボタンの下にあるプルダウンでフラクタル生成の繰り返し数を選択します.   
その後「CREATE FIELD」ボタンを押すと, 彩色するフラクタルの図形が作成されます.  
図形が作成できたら, 「CREATE MODEL」 ボタンを押して, モデルの作成に進みます.  
   
2. 量子アニーリングマシンのモデルの作成   
1つ目のフィールドにはモデルの名前を指定します.    
モデルの名前は何でも構いません.   
2つ目のフィールドにはスライス数を指定します.   
スライス数は別名トロッター数とも呼ばれ, 量子状態での重ね合わせをいくつ与えるかを指します.   
スライス数に10を指定すれば, 10個の重ね合わせが与えられます.     
スライス数を大きくすれば, 理論的には最適性は向上しますが, 収束性能が悪くなります.     
2つのフィールドに値を入力後, 「REGISTER」ボタンを押すと, 作成した図形がスピングラス模型に変換され, 
サーバーで量子アニーリングマシンのシミュレータによる処理が行われます.   

3. 結果の表示   
シミュレーターでの処理が完了したら, 画面が遷移します.   
「SHOW RESULT」ボタンを押すと, 作成した図形を彩色した結果が表示されます.    
このとき, 正解率がポップアップで表示されます.

## Use Case

![usecase](https://user-images.githubusercontent.com/27656483/52901458-15200e80-3247-11e9-802a-04a6d83efcab.png)

## Activity Transition

![snip20190217_31](https://user-images.githubusercontent.com/27656483/52901500-b7d88d00-3247-11e9-9d11-2d66de7fcf11.png)

## System Requirements
Android7.0(API24)

## Usage[on Mac]

    1. open with AndroidStudio
    2. cd /User/<your_name>/Library/Android/sdk/emulator
    3. ./emulator -list-avds
    4. ./emulator -avd <your_device_name> -dns-server 8.8.8.8
    5. run  

## Server side
constructed by Django with Django REST_Framework on Amazon EC2

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
- [Server Side] https://github.com/thanatoth/QASimulator_ServerSide
- [Solver] https://github.com/thanatoth/QASimulator_Solver
- [Emulator] https://github.com/thanatoth/QuantumAnnealing

## Author
Tomoya Tanaka
