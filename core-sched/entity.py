
import datetime
import time
import json

class Appointment:
    def __init__(self, id, pid, did):
        self.id = id
        self.pid = pid
        self.did = did
        self.complete = False
        self.start = False
        self.startTime = datetime.datetime.now()
        self.registerTime = datetime.datetime.now()
        self.completeTime = datetime.datetime.now()
        self.registerDoctorTime = 0
        self.startDoctorTime = 0
    def __str__(self):
        return "Appointment id {} pid {} did {} complete {} start {} startTime {} registerTime {} completeTime {} wait time {}".format(
            self.id, self.pid, self.did, self.complete, self.start, self.startTime, self.registerTime, self.completeTime,
            self.startDoctorTime - self.registerDoctorTime
        )
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
            sort_keys=True, indent=4)

class Doctor:
    def __init__(self, id, name):
        self.id = id
        self.name = name
        self.pending_queue = []
        self.complete_queue = []
        self.estimateTime = 20 * 60
        self.totalTime = 0
    def estimateQueueTime(self):
        return len(self.pending_queue) * self.estimateTime
    
    def finishAppointment(self, aid):
        self.pending_queue.remove(aid)
        self.complete_queue.append(aid)
    def __str__(self):
        return "doctor id: {} name: {} pending queue: {} complete queue: {} estimate time: {} total time {}".format(
            str(self.id), self.name, str(self.pending_queue), str(self.complete_queue), str(self.estimateTime), self.totalTime
            )
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
            sort_keys=True, indent=4)
