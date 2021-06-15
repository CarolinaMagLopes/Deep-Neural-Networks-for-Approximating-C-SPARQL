import os
import datetime
import pandas as pd
import matplotlib.pyplot as plt

def getTimestamp(tstamp):
	return tstamp.second + 60*tstamp.minute + 3600*tstamp.hour + 86400*tstamp.day + 2678400*tstamp.month

def getSector15(lon, lat):
	if lon < 10.13:
		gr = 2
	elif lon < 10.18:
		gr = 1
	else:
		gr = 0
	if lat < 56.12:
		sector = 3 - gr
	elif lat < 56.155:
		sector = 6 - gr
	elif lat < 56.17:
		sector = 9 - gr
	elif lat < 56.21:
		sector = 12 - gr
	else:
		sector = 15 - gr

	return sector

def getSector10(lon, lat):
	if lon < 10.18:
		gr = 0
	else:
		gr = 1
	if lat < 56.12:
		sector = 2 - gr
	elif lat < 56.155:
		sector = 4 - gr
	elif lat < 56.17:
		sector = 6 - gr
	elif lat < 56.21:
		sector = 8 - gr
	else:
		sector = 10 - gr

	return sector


def getParks():
	parks = {}
	with open("Parking/aarhus_parking_address.csv", "r") as fil:
		line = fil.readline()
		line = fil.readline()
		while line:
			text = line.split(',')
			parks[text[0]] = getSector(float(text[6]), float(text[5].replace('\n','')))
			line = fil.readline()
	return parks

session = {}
c = 1
times = set()

initial = getTimestamp(datetime.datetime.strptime('2014-08-01 00:00:00','%Y-%m-%d %H:%M:%S'))
end = getTimestamp(datetime.datetime.strptime('2014-10-01 00:00:00','%Y-%m-%d %H:%M:%S'))

sectors = [0]*10

file = open("Side/dataset.txt", 'r')

line = file.readline()
while line:
	fid = line.split("    ")[0].split(".")[0].replace("pollutionData","")
	coords = line.split("    ")[1]
	lon = float(coords.split(",")[1])
	lat = float(coords.split(",")[0])
	sector = getSector10(lon, lat)

	if sectors[sector-1] < 15:
		if fid != "201615":
			with open("Traffic/trafficData"+fid+".csv","r") as f:
				lin = f.readline()
				lin = f.readline()
				while lin:
					text = lin.split(",")
					hour = text[5].replace("T", " ")
					dat = getTimestamp(datetime.datetime.strptime(hour,'%Y-%m-%d %H:%M:%S'))
					times.add(dat)
					if dat not in session:
						session[dat] = []
					aux = "traffic average_measured_time " + text[1] + " " + str(sector) + " " + str(dat) + "\n"
					session[dat].append(aux)
					aux = "traffic average_speed " + text[2] + " " + str(sector) + " " + str(dat) + "\n"
					session[dat].append(aux)
					aux = "traffic vehicle_count " + text[6] + " " + str(sector) + " " + str(dat) + "\n"
					##aux = "traffic " + text[1] + " " + text[2] +  " " + text[6] + " " + str(sector) + " " + str(dat) + "\n"
					session[dat].append(aux)
					lin = f.readline()
				f.close()
			with open("Pollution/pollutionData"+fid+".csv","r") as f:
				lin = f.readline()
				lin = f.readline()
				while lin:
					text = lin.split(",")
					hour = text[7].replace("\n","")
					dat = getTimestamp(datetime.datetime.strptime(hour,'%Y-%m-%d %H:%M:%S'))
					times.add(dat)
					if dat not in session:
						session[dat] = []
					aux = "pollution " + text[0] + " " + text[1] +  " " + text[2] + " " + text[3] + " " + text[4] + " " + str(sector) + " " + str(dat) + "\n"
					session[dat].append(aux)
					lin = f.readline()
				f.close()
			sectors[sector-1] += 1

	line = file.readline()
	c += 1

file.close()

print("Time To Go")

week = 1
tim = 1
times = sorted(times)
writ = open("data"+str(week)+".txt","w")
for t in times:
	for line in session[t]:
		writ.write(line)
	if (tim % 2016) == 0:
		writ.close()
		week += 1
		tim = 0
		writ = open("data"+str(week)+".txt","w")
	tim += 1

writ.close()

df = pd.DataFrame(sectors, index=list(range(10)))

lines = df.plot.bar()
plt.show()

