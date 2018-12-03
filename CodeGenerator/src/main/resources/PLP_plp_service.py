#!/usr/bin/env python

from plp_monitors.srv import Plp
from plp_monitors.srv import PlpResponse
import rospy

plp_dict = {}


def handle_request(req):
    for item in req.plp_name:
        if item in plp_dict and plp_dict[item] >= 0:
            plp_dict[item] += req.count
    print "%s" % plp_dict[item]
    print plp_dict
    return PlpResponse(plp_dict[item])


def run_plp_service():
    global plp_dict
    rospy.init_node('check_plp_service')
    import inspect, os
    logging_file = os.path.dirname(
        os.path.abspath(inspect.getfile(inspect.currentframe())))
    with open(logging_file+"/plp.txt", 'r') as plp_list:
        list_plp = plp_list.read().splitlines()
        list_plp = list(set(list_plp))
    for i in range(len(list_plp)):
        plp_dict[list_plp[i]] = 0
    s = rospy.Service('plp_list', Plp, handle_request)
    print "Plp Service Ready!"
    rospy.spin()


if __name__ == "__main__":
    run_plp_service()
