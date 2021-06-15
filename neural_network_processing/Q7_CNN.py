
import tensorflow as ts
from tensorflow import keras
import numpy as np
import matplotlib.pyplot as plt 
import pandas as pd
import pickle
import time
import os

print("")
print("")
print("START")
print("")
print("")

# tensorboard --logdir=logs/

NAME = "Q7-model-{}".format(int(time.time()))
tensorboard = keras.callbacks.TensorBoard(log_dir = 'logs/{}'.format(NAME))


train = pd.read_pickle("./train.pickle")
test = pd.read_pickle("./test.pickle")

x_query_train = train['X'].tolist()
x_query_test = test['X'].tolist()

x_train = np.array(x_query_train)  
x_test = np.array(x_query_test)
  
y_query_train = train['Y'].tolist()
y_query_test = test['Y'].tolist()

y_train = np.array(y_query_train)
y_test = np.array(y_query_test)


model = keras.Sequential([
    
    keras.layers.Conv1D(filters=64, kernel_size=13, activation='relu', input_shape = x_train.shape[1:]),      
    keras.layers.MaxPooling1D(pool_size = 2), 

    keras.layers.Flatten(),
    keras.layers.Dense(128 , activation = "relu"),
    keras.layers.Dropout(0.5),

    keras.layers.Dense(64 , activation = "relu"),
    keras.layers.Dropout(0.5),
    
    keras.layers.Dense(10, activation = "sigmoid")
])



model.summary()

model.compile(optimizer="adam", loss = "binary_crossentropy", metrics=["binary_accuracy"])

model.fit(x_train, y_train , batch_size = 10, epochs = 50, shuffle = True, verbose = 2, validation_split= 0.1, callbacks=[tensorboard])

model.evaluate(x_test, y_test, batch_size = 10)




