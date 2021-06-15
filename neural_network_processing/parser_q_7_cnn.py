
import numpy as np
import matplotlib.pyplot as plt 
import pandas as pd
import pickle
import time
import os


possibilities = {}

possibilities["low_speed"] = 0
possibilities["normal_speed"] = 1
possibilities["high_speed"] = 2
possibilities["excess_speed"] = 3

possibilities["moviment"] = 4
possibilities["no_moviment"] = 5

possibilities['high_carbon'] = 6
possibilities['high_sulfure'] = 7

possibilities['clean_air'] = 8
possibilities['normal_air'] = 9
possibilities['saturated_air'] = 10
possibilities['unhealthy_air'] = 11 
possibilities['toxic_air'] = 12

                     

file_total = []
answers_total = []
typo = 0

files = [i for i in os.listdir("./Q7/Results17532") if i.endswith("txt")]

for read in files:
    print(read)

    with open(read, encoding="utf-8") as f:

        file = [ [ 0 for col in range(5) ] for row in range(130) ]  # 13*15
        answer = [ 0 for space in range(10) ]
        respostas = 0
        time = 0
        aux = -1.0

        for line in f.readlines():
            nline = line.split()
            setor = 1
            if len(nline) > 0 :
                # ANSWERS ----------------------------------------------------------------------------------------------------------
                if respostas == 1:
                    answer[int(nline[0])-1] = 1
                # DATA --------------------------------------------------------------------------------------------------------------
                
                else : 
                    # UPDATE TIME ----------------------------------------------------                    
                    if aux == -1.0 :
                        aux = int(nline[3])
                        time = 0
                    if aux < int(nline[3]) and time < 6:
                        aux = int(nline[3])
                        time += 1
                    
                    # UPDATE EVENT ----------------------------------------------------
                    setor = int(nline[2]) - 1
                    file[(13*setor) + possibilities[nline[1]] ][time] = 1

            else :
                respostas = 1

        file_total.append(file)
        answers_total.append(answer)



dataframe = pd.DataFrame({ 
    'X' : file_total ,
    'Y' : answers_total
 })

train = dataframe.sample(16000)
test = dataframe.drop(train.index)


test.to_pickle("./Q7/test.pickle")
train.to_pickle("./Q7/train.pickle")



