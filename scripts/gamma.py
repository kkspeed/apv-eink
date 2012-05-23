#!/usr/bin/env python
# Generate the gamma.h

def gamma_val(gamma):
	return [min(255, int((255.0 * (i / 255.0)**(1.0 / gamma)) + 0.5))
			 for i in xrange(256)]

def print_val(num, array, fo):
	print >> fo, "{\n"
	for i in xrange(len(array)):
		print >>fo, "%d," % array[i]	
		if (i + 1) % num == 0:
			print >> fo, "\n"
	print >> fo, "},\n"

def make_gamma(gamma, fo):
	print >> fo, "// Gamma: %f\n" % gamma
	print_val(8, gamma_val(gamma), fo)

def output():
	fo = open("gamma.h", "w+")
	print >> fo, "const int gamma_map[][256] = {\n"
	for i in [1.0, 0.8, 0.6, 0.45, 0.35, 0.25, 0.15, 0.1, 0.05]:
		make_gamma(i, fo)
	print >> fo, "};\n"


output()
