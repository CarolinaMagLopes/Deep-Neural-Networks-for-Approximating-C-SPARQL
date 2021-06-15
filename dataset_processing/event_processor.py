import os
import glob

direc = glob.glob("*.txt")



for f in direc:
	wr = open("Events/"+f, 'w')
	with open(f, 'r') as file:
		line = file.readline()
		while line:
			text = line.split()
			if text[0] == "pollution":
				if int(text[2]) >= 100 and int(text[3]) >= 120 and int(text[4]) >= 120:
					wr.write("pollution toxic_air " + text[6] + " " + text[7] + "\n")
				elif int(text[2]) < 100 and int(text[3]) >= 120 and int(text[4]) >= 120:
					wr.write("pollution unhealthy_air " + text[6] + " " + text[7] + "\n")
				elif int(text[3]) >= 120:
					wr.write("pollution high_carbon " + text[6] + " " + text[7] + "\n")
				elif int(text[4]) >= 120:
					wr.write("pollution high_sulfure " + text[6] + " " + text[7] + "\n")
				elif int(text[2]) < 70 and int(text[3]) < 70 and int(text[4]) < 70:
					wr.write("pollution clean_air " + text[6] + " " + text[7] + "\n")
				elif int(text[1]) >= 140 and int(text[5]) >= 150:
					wr.write("pollution saturated_air " + text[6] + " " + text[7] + "\n")
				else:
					wr.write("pollution normal_air " + text[6] + " " + text[7] + "\n")
			else:
				line = file.readline()
				avgSpeed = int(line.split()[2])
				line = file.readline()
				moviment = int(line.split()[2])
				if avgSpeed < 30:
					wr.write("traffic low_speed " + text[3] + " " + text[4] + "\n")
				elif avgSpeed < 50:
					wr.write("traffic normal_speed " + text[3] + " " + text[4] + "\n")
				elif avgSpeed < 65:
					wr.write("traffic high_speed " + text[3] + " " + text[4] + "\n")
				else:
					wr.write("traffic excess_speed " + text[3] + " " + text[4] + "\n")

				if moviment == 0:
					wr.write("traffic no_moviment " + text[3] + " " + text[4] + "\n")
				else:
					wr.write("traffic moviment " + text[3] + " " + text[4] + "\n")

			line = file.readline()






