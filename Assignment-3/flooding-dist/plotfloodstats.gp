#
set term png  
set output "NZandAustraliaDouble.png"
set title "Comparison of 3 flooding protocols"
set xlabel "Running time (seconds)"
set ylabel "Delivery efficiency (%)"
#
set grid
set yrange [0:]
#
plot	\
	"result.flood3"		title "flooding3" with linespoints
#
pause -1
