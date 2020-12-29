
from flask import Flask,jsonify,render_template,request,session
from flask_cors import CORS
from flask_socketio import SocketIO,emit,join_room
import requests
import json
import time
import datetime
from entity import Doctor,Appointment
import random



app = Flask(__name__,static_url_path="")
app.config['SECRET_KEY'] = 'secret!'
cors = CORS(app, resources={r"/*": {"origins": "*"}})

doctors = [
    Doctor(1, "Alice"),
    Doctor(2, "Bob"),
    Doctor(3, "Peter"),
    Doctor(4, "Sunrise"),
    Doctor(5, "Sky"),
]

global_aid = 1
appointments = []

@app.route("/RMP/<path:path_name>", methods = ["GET"])
def rmp_get(path_name):
    print("rmp get")
    print(request.path)
    url = "http://202.120.40.8:30611/" + request.path.split("RMP/")[1]
    print(url)
    data = requests.get(url = url)
    print(data)
    r = json.loads(data.text)
    print(r)
    return jsonify(r)
    
@app.route("/RMP/<path:path_name>", methods = ["POST"])
def rmp_post(path_name):
    print("rmp post")
    print(request.path)
    data = request.get_data()
    print(data)
    r = json.loads(data)
    url = "http://202.120.40.8:30611/" + request.path.split("RMP/")[1]
    response = requests.post(url = url, data = data)
    print(response.text)
    return jsonify(json.loads(response.text))

@app.route("/time")
def default_time():
    now = datetime.datetime.now()
    print(now)
    after = now + datetime.timedelta(minutes=60)
    print(after)
    time_str = after.strftime('%Y-%m-%d %H:%M:%S')
    delta = after - now
    print(delta.seconds)
    date = datetime.datetime.strptime(time_str, '%Y-%m-%d %H:%M:%S')
    print(date)
    return time_str

def printDoctors():
    for d in doctors:
        print(d)

def printAppointments():
    for a in appointments:
        print(a)

def doctorEstimateTime(doctor, flag):
    if flag == "last":
        last_aid = doctor.pending_queue[0]
        a = list(filter(lambda x: x.id == last_aid and x.complete == True, appointments))[0]
        return (a.completeTime - a.startTime).seconds
    elif flag == "poisson":
        times = []
        last_aid = doctor.pending_queue[0]
        a = list(filter(lambda x: x.id == last_aid and x.complete == True, appointments))[0]
        times.append((a.completeTime - a.startTime).seconds)
        for aid in doctor.complete_queue:
            a = list(filter(lambda x: x.id == aid and x.complete == True, appointments))[0]
            times.append((a.completeTime - a.startTime).seconds)
        return sum(times) * 1.0 / len(times)
    return 0


@app.route("/register/doctors")
def get_doctors():
    doctor_json = [d.toJSON() for d in doctors]
    return json.dumps(doctor_json)
        

@app.route("/register/require")
def require_register():
    args = request.args
    pid = eval(args["pid"])
    did = 0
    minTime = 99999

    # 寻找估计时间最短的医生
    for doctor in doctors:
        if doctor.estimateQueueTime() < minTime:
            did = doctor.id
            minTime = doctor.estimateQueueTime()

    #FIXME: 用RMP的id?
    global global_aid
    aid = global_aid
    global_aid += 1

    # 创建新的appointment
    new_appointment = Appointment(aid, pid, did)
    appointments.append(new_appointment)


    # 在医生的对应appointment里插入
    doctor = list(filter(lambda x: x.id == did, doctors))[0]
    doctor.pending_queue.append(aid)
    new_appointment.registerDoctorTime = doctor.totalTime
    printDoctors()
    printAppointments()
    return json.dumps({"name": doctor.name, "estimate": doctor.estimateQueueTime()})


@app.route("/register/start")
def start_appointment():
    args = request.args
    pid = eval(args["pid"])
    a = list(filter(lambda x: x.pid == pid and x.start == False, appointments))[0]

    did = a.did
    doctor = list(filter(lambda x: x.id == a.did, doctors))[0]
    if doctor.pending_queue[0] != a.id:
        printDoctors()
        printAppointments()
        return jsonify({"status": "fail"})

    a.start = True
    # FIXME: use other time
    a.startTime = datetime.datetime.now()
    a.startDoctorTime = doctor.totalTime
    printDoctors()
    printAppointments()
    return jsonify({"status": "success"})



@app.route("/register/over")
def stop_appointment():
    args = request.args
    pid = eval(args["pid"])
    a = list(filter(lambda x: x.pid == pid and x.start == True and x.complete == False, appointments))[0]
    a.complete = True
    # 随机决定问诊时间
    delta = datetime.timedelta(minutes=random.normalvariate(20,10 ))
    a.completeTime = a.startTime + delta

    doctor = list(filter(lambda x: x.id == a.did, doctors))[0]
    # 预计问诊时间，目前直接采用最后一次问诊时间，之后可以改成历史问诊时间平均数，或者泊松分布等
    doctor.estimateTime = doctorEstimateTime(doctor, "poisson")
    doctor.totalTime += delta.seconds
    doctor.finishAppointment(a.id)
    printDoctors()
    printAppointments()
    return jsonify({"status": "success"})

if __name__ == '__main__':
    app.run(port = 3002, host = '0.0.0.0')