#!/usr/bin/env python

from plp_monitors.srv import Resources
from plp_monitors.srv import ResourcesResponse
import rospy

resources_dict = {}


def handle_request(req):
    result = False
    for item in req.resource_name:
        if item in resources_dict and resources_dict[item] == "True":
            result = True
            resources_dict[item] = "False"
    if req.release is True:
        result = True
        for item1 in req.resource_name:
            resources_dict[item1] = "True"
    print "%s" % result
    print resources_dict
    return ResourcesResponse(result)


def run_resources_service():
    global resources_dict
    rospy.init_node('check_resources_service')
    import inspect, os
    logging_file = os.path.dirname(
        os.path.abspath(inspect.getfile(inspect.currentframe())))
    with open(logging_file+"/resources.txt", 'r') as resource_list:
        list_resource = resource_list.read().splitlines()
        list_resource = list(set(list_resource))
    for i in range(len(list_resource)):
        resources_dict[list_resource[i]] = "True"
    s = rospy.Service('resources_list', Resources, handle_request)
    print "Resources Service Ready!"
    rospy.spin()


if __name__ == "__main__":
    run_resources_service()
