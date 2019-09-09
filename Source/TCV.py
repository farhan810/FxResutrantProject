# Bidirectional serial/server communication code
# TableCheck phase 1 02/25/2014
# Phase 2 with blkmap 07/07/2014
# Conversion to single table change message to server, changing over to Database operations  08/5/2014
# Update command from tablet loads tableall table to the HW panels 08/27/14
# V1.0 reads the 204 byte update function output message from HW panel then sends tha data on to the server and updates the local table map 09/22/2014
# 11-01-2014 Major release with checking for every possible table location for HW panel update command processing
# 11-28-2014 minor tweaks to listready length issues
# 12-05-2014 Removed the "=" from "T1=" so value saved was "T1" not "T1="
# 12-13-2014 Fixed the bld1 = mystring[1]; index out of range error
# 1-23-2015  To prevent file corruption, updated /etc/fstab to prevent most writes to the SD card
# 2-4-2015   Merge two Pi's into one image Version 1.5
# 6-3-2015   Added ritblu.py to enable PC command interface to this Raspi Version 1.7

import serial
from time import sleep

import urllib.request
#from urllib.request import urlopen
from urllib.error import HTTPError,URLError
import re
import subprocess
import requests
import random
import json
import socket
import threading
import time

# Initialize the serial port
ser = serial.Serial(
    port='/dev/ttyAMA0',
    baudrate=1200,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS,
    timeout=0
)

server_ip =''
threadCnt = 0
threadOver = 0
def main():
    global server_ip
    global threadCnt
    global threadOver

	##  County Line has 64 tables
    tableallo = [0] # out going table status
    tableall = [0] # Incoming table status

##    tableallo now - '39', '1', '85', 'green', '43', '3', '85', 'green', '43', '1', '85', 'green', '47', '3', '85', 'green',
#                 '47', '1', '85', 'green', '51', '4', '85', 'green', '51', '3', '85', 'green', '59', '3', '85', 'green',
#                 '67', '1', '85', 'green', '71', '4', '85', 'green', '71', '3', '85', 'green', '71', '2', '85', 'green',
#                 '71', '1', '85', 'green', '63', '4', '85', 'green', '63', '2', '85', 'green', '87', '1', '85', 'green',
#                 '91', '4', '85', 'green', '91', '3', '85', 'green', '91', '2', '85', 'green', '91', '1', '85', 'green',
#                 '99', '4', '85', 'green', '99', '1', '85', 'green', '103', '3', '85', 'green', '111', '4', '85', 'green',
#                 '119', '4', '85', 'green', '119', '2', '85', 'green', '123', '4', '85', 'green', '123', '2', '85', 'green',
#                 '135', '4', '85', 'green', '135', '2', '85', 'green', '139', '4', '85', 'green', '139', '2', '85', 'green',
#                 '143', '4', '85', 'green', '143', '2', '85', 'green', '147', '3', '85', 'green', '147', '1', '85', 'green',
#                 '151', '3', '85', 'green', '151', '1', '85', 'green', '155', '4', '85', 'green', '155', '2', '85', 'green',
#                 '159', '4', '85', 'green', '159', '2', '85', 'green', '163', '4', '85', 'green', '163', '2', '85', 'green',
#                 '167', '3', '85', 'green', '167', '1', '85', 'green', '171', '3', '85', 'green', '171', '1', '85', 'green',
#                 '175', '1', '85', 'green', '179', '3', '85', 'green', '179', '1', '85', 'green', '183', '3', '85', 'green',
#                 '187', '1', '85', 'green', '191', '3', '85', 'green', '191', '1', '85', 'green', '195', '2', '85', 'green',
#                 '199', '4', '85', 'green', '199', '2', '85', 'green', '203', '4', '85', 'green', '203', '2', '85', 'green',
#                 '207', '4', '85', 'green', '207', '2', '85', 'green', '211', '4', '85', 'green', '211', '2', '85', 'green'



##    tableallo ** OLD ***= [0x27, 1, 0x55, 'green', 0x2b, 3, 0x55, 'green', 0x2b, 1, 0x55, 'green', 0x2f, 3, 0x55, 'green', 
##                0x2f, 1, 0x55, 'green', 0x33, 4, 0x55, 'green', 0x33, 3, 0x55, 'green', 0x3b, 3, 0x55, 'green', \
##                0x43, 1, 0x55, 'green', 0x47, 4, 0x55, 'green', 0x47, 3, 0x55, 'green', 0x47, 2, 0x55, 'green', \
##                0x47, 1, 0x55, 'green', 0x3f, 4, 0x55, 'green', 0x3f, 2, 0x55, 'green', 0x57, 1, 0x55, 'green', \
##                0x5b, 4, 0x55, 'green', 0x5b, 3, 0x55, 'green', 0x5b, 2, 0x55, 'green', 0x5b, 1, 0x55, 'green', \
##                0x63, 4, 0x55, 'green', 0x63, 2, 0x55, 'green', 0x67, 3, 0x55, 'green', 0x6f, 4, 0x55, 'green', \
##                0x77, 4, 0x55, 'green', 0x77, 2, 0x55, 'green', 0x7b, 4, 0x55, 'green', 0x7b, 2, 0x55, 'green', \
##                0x87, 4, 0x55, 'green', 0x87, 2, 0x55, 'green', 0x8b, 4, 0x55, 'green', 0x8b, 2, 0x55, 'green', \
##                0x8f, 4, 0x55, 'green', 0x8f, 2, 0x55, 'green', 0x93, 3, 0x55, 'green', 0x93, 1, 0x55, 'green', \
##                0x97, 3, 0x55, 'green', 0x97, 1, 0x55, 'green', 0x9b, 4, 0x55, 'green', 0x9b, 2, 0x55, 'green', \
##                0x9f, 4, 0x55, 'green', 0x9f, 2, 0x55, 'green', 0xa3, 4, 0x55, 'green', 0xa3, 3, 0x55, 'green', \
##                0xa7, 3, 0x55, 'green', 0xa7, 1, 0x55, 'green', 0xab, 3, 0x55, 'green', 0xab, 1, 0x55, 'green', \
##                0xaf, 1, 0x55, 'green', 0xb3, 3, 0x55, 'green', 0xb3, 3, 0x55, 'green', 0xb3, 1, 0x55, 'green', \
##                0xb7, 3, 0x55, 'green', 0xbb, 1, 0x55, 'green', 0xbf, 3, 0x55, 'green', 0xbf, 1, 0x55, 'green', \
##                0xc3, 2, 0x55, 'green', 0xc7, 4, 0x55, 'green', 0xc7, 2, 0x55, 'green', 0xcb, 2, 0x55, 'green', \
##                0xcf, 4, 0x55, 'green', 0xcf, 2, 0x55, 'green', 0xd3, 4, 0x55, 'green', 0xd3, 2, 0x55, 'green']


##THE NEW BLKMAP:
#    blkmap = [39, 1, 0, 43, 5, 1, 47, 5, 3, 51, 12, 5, 59, 4, 6, 67, 1, 7, 71, 15, 11, 63, 10, 13, 87, 1, 14, \
#                 91, 15, 18, 99, 9, 20, 103, 4, 21, 111, 8, 22, 119, 10, 24, 123, 10, 26, 135, 10, 28, 139, 10, 30, \
#                 143, 10, 32, 147, 5, 34, 151, 5, 36, 155, 10, 38, 159, 10, 40, 163, 10, 42, 167, 5, 44, 171, 5, 46, \
#                 175, 1, 47, 179, 5, 49, 183, 4, 50, 187, 1, 51, 191, 5, 53, 195, 2, 54, 199, 10, 56, 203, 10, 58, \
#                 207, 10, 60, 211, 10, 62]


##THE TEST BLKMAP:
    blkmap1 = [39, 1, 0, 43, 5, 1, 47, 5, 3, 51, 12, 5, 59, 4, 7, 63, 10, 8, 67, 1, 10, 71, 15, 11, 87, 1, 15, \
                 91, 15, 16, 99, 9, 20, 103, 4, 22, 111, 8, 23, 119, 10, 24, 123, 10, 26, 135, 10, 28, 139, 10, 30, \
                 143, 10, 32, 147, 5, 34, 151, 5, 36, 155, 10, 38, 159, 10, 40, 163, 10, 42, 167, 5, 44, 171, 5, 46, \
                 175, 1, 48, 179, 5, 49, 183, 4, 51, 187, 1, 52, 191, 5, 53, 195, 2, 55, 199, 10, 56, 203, 10, 58, \
                 207, 10, 60, 211, 10, 62]

##    blkmap = [0x27, 1, 0x2b, 5, 0x2f, 5, 0x33, 'c', 0x3b, 4, 0x43, 1, 0x47, 'f', 0x3f, 'a', 0x57, 1, 0x5b, 'f', \
##              0x63, 9, 0x67, 4, 0x6f, 8, 0x77, 'a', 0x7b, 'a', 0x87, 'a', 0x8b, 'a', 0x8f, 'a', 0x93, 5, 0x97, 5, \
##              0x9b, 'a', 0x9f, 'a', 0xa3, 'a', 0xa7, 5, 0xab, 5, 0xaf, 1, 0xb3, 5, 0xb7, 4, 0xbb, 1, 0xbf, 5, \
##              0xc3, 2, 0xc7, 'a', 0xcb, 'a', 0xcf, 'a', 0xd3, 'a']


    #server_ip = getServerIp()
    getServerIp()
    while (len(server_ip) == 0 and threadOver == 0):
    	#print ('Scanning for server')
    	time.sleep(3)
    if (len(server_ip) == 0):
        return 0
    
    urlmap = 'http://'+server_ip+':8080/tableservice/tablemap'
    
    ## define global variables
    inblock = '0x0'
    updatehex = '0x0'
    psloop = 'zero'

    # Read table map data from file
    #infile = open('/usr/local/bin/tablemap.txt', 'r')
    #file_contents = infile.read()
    #infile.close()
    netin = ""
    try:
        netin = urllib.request.urlopen(urlmap, data=None, timeout=1).read()
        file_contents = netin.decode(encoding = 'utf-8')  ## This does the conversion from bytes to byte string
        # Convert the data from string to list string values
        tableallo = file_contents.split()
    except HTTPError as e:
        print('Read HTTP error')
        return main
    except URLError as e:
        print('Read URL error')
        return main
    except Exception:
        print('General Exception error')
        return main

    tableall = tableallo
#    print('TABLEALL: ', tableall)


    # Build the table that is used to verify table state data stay's in order


    tabchk = [0] * int(len(tableall) / 4)
    for tabc in range(0, int(len(tableall)/4), 1):
        tabchk[tabc] = 'T' + str(tabc + 1)

#    print(tabchk)


    # Calculate length value for netoutbuf
    lengnob = int((len(tableall) / 4))

    # generate netoutbuf list
    netoutbuf = [''] * lengnob

##    print('This is what is in the file', file_contents, 'Lenght of tableallo', len(tableallo))    
    # Convert the three number values to integer in each table record
    for f in range(0, len(tableallo), 4): 
        tableallo[f] = int(tableallo[f])
        tableallo[f + 1] = int(tableallo[f + 1])
        tableallo[f + 2] = int(tableallo[f + 2])
    ## initialize both the incoming and outgoing tables for the first time
        tableall[f] = tableallo[f]
        tableall[f + 1] = tableallo[f + 1]
        tableall[f + 2] = tableallo[f + 2]
        tableall[f + 3] = tableallo[f + 3]

# The next 30 lines of code generates the block map.  First counts the blocks then assigns 
# a hex value based on which sections are present in that block.

    BMT = 0
    bmpos = 0
    blkcntr = 0
    NBM = 0
    FBM = 0
    tblcnt = 0
    pickle = 1

# The following code counts the number of blks to calculate the size of the storage buffer

    for l in range (0, len(tableall), 4):
        NBM = FBM
        FBM = tableall[l]
        if NBM != FBM:
            blkcntr = blkcntr + 1
## Generate the block map
    blkmap = [0] * blkcntr * 3
## The following code encodes the single bit magnitude and adds all those values together
## so all the sections are encoded via a hex digit for a block.  The main program uses
## to decode the actual location of all of the sections for a given block.
## EXAMPLE: If all the sections are used, the hex value is "F"
##          If only section 4 and section 2 are used, the hex value is "A"
##          If only sections 3 and 1 are used, the hex value is "5"
    for l in range (0, len(tableall), 4):
#        print(' ')
        FBM = tableall[l]   # FBM is the block  number from tableall
#        print('Start of table count in a block check: ', tblcnt, 'Current block: ', FBM)
        FSEC = tableall[(l + 1)] # FSEC is the section number
#        print('FBM: ', FBM, 'blkmap[len(blkmap) - 3]', blkmap[len(blkmap) - 3])
        if FSEC == 1:
            # BMT is the encoded value that is made up of the sections in a block
            BMT = BMT + 1
            if FBM != tableall[(len(tableall) - 4)]:
                tblcnt = tblcnt + 1
        if FSEC == 2:
            BMT = BMT + 2
            if FBM != tableall[(len(tableall) - 4)]:
                tblcnt = tblcnt + 1
        if FSEC == 3:
            BMT = BMT + 4
            if FBM != tableall[(len(tableall) - 4)]:
                tblcnt = tblcnt + 1
        if FSEC == 4:
            BMT = BMT + 8
            if FBM != tableall[(len(tableall) - 4)]:
                tblcnt = tblcnt + 1
## This prevents the last loop error
        if (l + 4) != len(tableall):
#            print('lenght tableall: ', len(tableall), 'l is: ', l)
          
            NBM = tableall[(l + 4)] # NBM next block value
#        print('FBM: ', FBM, 'NBM: ', NBM, 'BMT: ', BMT, 'tablecnt: ', tblcnt)
## Here is where the looping happens on the same block for multiple sections
        if (FBM != NBM):
            blkmap[bmpos] = FBM
            blkmap[(bmpos + 1)] = BMT
# Take away one table from the count to compansate for the start position being zero

#            print('I just took a count away from tblcnt 1')
            if (bmpos + 5) < len(blkmap):
                blkmap[(bmpos + 5)] = tblcnt
#            print('Current blk no.: ', FBM, 'Saved BMT value: ', BMT, 'saved end table count value as the start for the next block: ', tblcnt)
            BMT = 0
            bmpos = (bmpos + 3)
        # THis is to adjust for the offset on the first pass through
#        if l == 0:
#            print('I just took a count away from tblcnt for the first pass through.')
#            tblcnt = tblcnt - 1 

#    print('loop done')
    blkmap[bmpos] = FBM
    blkmap[(bmpos + 1)] = BMT
       # This compansates for the table starting at zero
#    tblcnt = tblcnt - 1
    blkmap[(bmpos + 2)] = tblcnt
#    print('BLKMAP hand generated: ', blkmap1)
#    print('BLKMAP algorythum generated: ', blkmap)


    ## retrieve the read and write urls for the server from the location.txt file    
#    infile = open('/usr/local/bin/server_location.txt', 'r')
    infile = open('/usr/local/bin/urls.js', 'r')
    urlall = infile.read()
#    print('urlall: ', urlall)
    infile.close()

    url2 = urlall.split()
#    url = url2[1]
#    urlx = url2[0]
    url = url2[12]    # Grab the reader_pl url
    url = url[:-2]   # Remove the last 2 characters of the url string which is a ";
    url = url[1:]   # Remove the first character of the url string which is a "

    urlx = url2[16]      # Grab the writer_pl url
    urlx = urlx[:-2]    # Remove the last 2 characters of the url string which is a ";
    urlx = urlx[1:]   # Remove the first character of the url string which is a "


#    url = 'http://192.168.1.2:8080/table'
#    urlx = 'http://192.168.1.2:8080/table'
    if len(server_ip) == 0:
#    	server_ip = getServerIp()
        getServerIp()
        while (len(server_ip) == 0 and threadOver == 0):
            print ('Scanning for server')
            time.sleep(3)
        if (len(server_ip) == 0):
            return 0
	
    url = 'http://'+server_ip+':8080/tableservice/table'
    urlx = 'http://'+server_ip+':8080/tableservice/tablehw'
    
    #url = 'http://192.168.1.210:8080/tableservice/table'
    #urlx = 'http://192.168.1.210:8080/tableservice/tablehw'

    ## restid = url2[2]

    print('reader.s:  ', url, 'writer.s:  ', urlx)

    ## retrieve the restuarant ID and the customer ID from the customer_data.js file    
    infile2 = open('/usr/local/bin/customerdata.js', 'r')
    cusdat = infile2.read()
    infile2.close()

    cusdat2 = cusdat.split()
    rest_id = cusdat2[7]
    cus_id = cusdat2[3]

    cus_id = cus_id[:-1]                  #remove the last character, a semicolon, from the string
    rest_id = rest_id[:-1]                  #remove the last character, a semicolon, from the string

    tablenumsave = 1
#&  The next two lines are for test only
#&    cus_id = 2
#&    rest_id = 1

##    print('cus_id: ', cus_id, 'rest_id: ', rest_id)



    ## restid = url2[2]

#    print('HERE IS TABLEALLO FROM THE FILE: ', tableallo)
    
    dog = "hack"
    mystring = b''
    response = ""
    startcurrentsection = '0x55'
    currentblkval = ""
    newblkval = ""

    print('TABLECHECK Server Interface adapter COPY RIGHT 2017 Version: V3.0 Aug 25, 2017')
##    dog = input('Press enter to start the program')
##    MAIN LOOP   ##   MAIN LOOP   ##    MAIN LOOP
    while dog != ("y" or "Y"):
##        write_server(urlx, tableallo, blkmap)
        subprocess.call(['john.sh'])

#        print('psloop before read_server 7: ', psloop)
        response = read_server(psloop, url, tableall, tableallo, inblock, updatehex, tabchk)
        print('Read response ', response)
        if(response == 'NOSERVER'):
            return main
#        print('psloop after read_server 8', psloop)
#        print('psloop before write_server 9: ', psloop)
        response = write_server(urlx, tableallo, blkmap, netoutbuf, mystring, rest_id, cus_id, tablenumsave)
        print('write response ', response)
        if(response == 'NOSERVER'):
            return main
#        print('psloop after write_server: 10', psloop)
        sleep(.5)
#        print('Circled main loop!!!')
##        print('URLX: ', urlx)

##
def write_server(urlx, tableallo, blkmap, netoutbuf, mystring, rest_id, cus_id, tablenumsave):


    # The following reads the data from the panel via serial port
    # the readline(12) sets a limit of three incoming 4 byte messages from the serial port
    response = (ser.readline(800))
    # This is where the serial message is added to the buffer.  The buffer is reduced by four bytes each time a message is sent
    # the 'your file was saved' is received from the server

    mystring = mystring + response

#    mystring, response = rdserin(mystring, j)

#    if psloop == 'one':
#        print('Top of write_server mystring lenght 3: ', len(mystring))
#        print('tablenum: ', tablenum, 'tablenumsave: ', tablenumsave)
#        tablenum = tablenumsave
       
#        network_out(tableallo, rest_id, cus_id, tablenum, ncolor, jet1, jet2, urlx, mystring)

#        return write_server
#    print('Top of write_server mystring: ', mystring)
    if len(mystring) == 0:
#        print('Top of write_server mystring lenght 2: ', len(mystring))
        return write_server


#    print('response value after read buffer', response, 'response lenght: ', len(response))


#    mystringcnt = 0
#    print('mystring[0]', mystring[0])
#    print('mystring[1]', mystring[1])
#    print('mystring[2]', mystring[2])
#    print('mystring[3]', mystring[3])


    jet1 = 0
    jet2 = 0
    loop = 0
#    print('zeroed findmatch')
    findmatch = 0
    newcol = 'pink'
    lastcolor = 'pokadot'
    newsec = 0
    tablenum = 0
    nowval = (blkmap[0])
    curseccol = 0
    blkloc = 0
    blkcase = 0
    cnttable = 0
    curblk = (tableallo[0]) # This is for the first pass of the while loop below
                ##On exit from this while loop, curblk will have which block changed 
#    print('Intitalize bpos to zero before message is parsed')
    bpos = 0
    bytesby = 0
    ackfix = len(mystring)
    jetfixend = blkmap[len(blkmap) - 3]
    jet1, jet2 = fourM2B(mystring, jet1, jet2)
    jetfix = blkmap[0]
    for j in range(0, ackfix, 4):    
#        print('sleep', 'j = ', j, 'ackfix: ', ackfix, 'findmatch: ', findmatch, 'jet1: ', jet1, 'len(mystring): ', len(mystring))
        if j > ackfix:
           print('mystring lenght ERROR from serial input buffer: ', mystring, 'mystring length', len(mystring), 'j Value: ', j)
           return write_server
##############################################################
# Check here to see if the incoming block is valid for the layout
# and gets the next correct block to hand off to the next subroutine 
####################################################################
#        print('jet1 top of j loop and type: ', jet1, type(jet1), 'jet2: ', jet2, type(jet2), 'len(mystring): ', len(mystring), 'findmatch: ', findmatch)
#        jetfix = blkmap[0]
#       FIND the block that matches the incoming block
#        print('BLOCKMAP: ', blkmap )
#        print('we are in the J loop!')
        dblbrk = 0
        # The following prevents a random error with the first table not updating
        if bpos == 0 and blkcase == 0 and jetfix == jet1:
            blkcase = blkmap[bpos + 1]
            tablenum = blkmap[bpos + 2]
            bpos = bpos + 3
#            print('EXIT a way I missed?')
#            jetfix = blkmap[bpos]
#            print('If bpos and blkcase = 0, jetfix and jet1: ', jetfix, jet1, 'len(mystring): ', len(mystring))
#            print('WHERE AM I???')
        else:
            while jetfix != jet1:  ### this is comparing a block from this layout to the incoming block
#                print('Entering While, bpos is', bpos)
#                if jet2 != 85:
#                    print('jet1 and jet2: ', jet1, jet2, 'len(mystring: ', len(mystring))
#                    print('mystring[0]', mystring[0])
#                    print('mystring[1]', mystring[1])
#                    print('mystring[2]', mystring[2])
#                    print('mystring[3]', mystring[3])
#                    print('bpos: ', bpos)
                jetfixold = jetfix
                jetfix = blkmap[bpos]
                blkcase = blkmap[bpos + 1]
                tablenum = blkmap[bpos + 2]
#            print('bpos: ', bpos,'jetfix', jetfix, 'jet1: ', jet1, 'blkcase: ', blkcase, 'tablenum: ', tablenum)
#                if jetfixold != jet1:
                bpos = bpos + 3
            # this is for the last loop of the last block location
                if jetfix == jetfixend and jetfix == jet1:
#                    print('EXIT - This is the great excape! mystring: ', mystring)
                    break
            # this is for when all the blocks for this layout have been checked and none match, leave loop, do not send out
            # a message and grab another message to look at
                if bpos >= len(blkmap):
                    dblbrk = 1
#                    print('EXIT - bpos has exceed len(blkmap)', 'dblbrk: ', dblbrk)
                    break
#        print('EXIT - Normal While exiting. jet1 and jetfix are: ', jet1, jetfix)
        if dblbrk != 1:
#            print('FRIDAY 3: ', '  dblbrk: ', dblbrk)
            dblbrk = 0

## END OF write_server
##
##############################################################################################
##############################################################################################
##############################################################################################




######################################################################################
# START of update_col
#######################################################################################


#def update_col(blkcase, tableallo, findmatch, jet1, jet2, rest_id, cus_id, urlx, mystring, tablenum):

##        print('Blockcase: ', blkcase)
#            print('I LOOPED PAST HERE 2!')

        ## In section 4 below, all the bits are masked off except bits 0 and 1.  The color is determined by 0, 1, 2 or 3.
        ## The following is used to detemine if a section 4 table was updated
##       Section 4
#            if bpos >= 0 and bpos <= 12:   

            findmatch = tablenum * 4
#            print('STR of SEC4 findmatch = ', findmatch, 'table count: ', tablenum, 'Block case: ', blkcase, 'bpos: ', bpos)
            if blkcase == 15 or blkcase == 14 or blkcase == 13 or blkcase == 12 or blkcase == 11 or blkcase == 10 or blkcase == 9 or blkcase == 8:
#                print('process section 4 - tablenum', tablenum)     
                nowcol = jet2 & 3
                if nowcol == 0:
                    newcol = 'yellow'
                if nowcol == 1:
                    newcol = 'green'
                if nowcol == 2:
                    newcol = 'red'
                if nowcol == 3:
                    newcol = 'gray' 

                lastcolor = tableallo[findmatch + 3]
                tableallo[findmatch + 3] = str(newcol)
                tableallo[findmatch + 2] = jet2
                tablenum = tablenum + 1
#                print('One just added to tablenum sec 4')
                if lastcolor != newcol:
#                    print('lastcolor != newcol sec 4')
                    ncolor = tableallo[findmatch + 3]
                    network_out(tableallo, rest_id, cus_id, tablenum, ncolor, jet1, jet2, urlx, mystring)
#                print('HERE 1')
                findmatch = findmatch + 4
#                print('4 just added to findmatch sec 4')
        ## In section 3 below, all the bits are masked off except bits 2 and 3.  The color is determined by 0, 4, 8 or c.

##       Section 3 
#            print('I LOOPED PAST HERE 4 section 3! findmatch = ', findmatch, 'table count: ', tablenum)
            if blkcase == 15 or blkcase == 14 or blkcase == 13 or blkcase == 12 or blkcase == 7 or blkcase == 6 or blkcase == 5 or blkcase == 4:
#                print('process section 3 - tablenum', tablenum, 'masked jet2: ', (jet2 & 12))
                nowcol = (jet2 & 12)
                if nowcol == 0:
                    newcol = 'yellow'
                if nowcol == 4:
                    newcol = 'green'
                if nowcol == 8:
                    newcol = 'red'
                if nowcol == 12:
                    newcol = 'gray'


        ## The following is used to detemine which section 3 table was updated

                lastcolor = tableallo[findmatch + 3]
                tableallo[findmatch + 3] = str(newcol)
                tableallo[findmatch + 2] = jet2
                tablenum = tablenum + 1
#                print('One just added to tablenum sec 3')
                if lastcolor != newcol:
#                    print('lastcolor != newcol sec 3')
                    ncolor = tableallo[findmatch + 3]
                    network_out(tableallo, rest_id, cus_id, tablenum, ncolor, jet1, jet2, urlx, mystring)
#                print('HERE 2')
                findmatch = findmatch + 4
#                print('4 just added to findmatch sec 3')

##       Section 2
#            print('I LOOPED PAST HERE 4 section 2! findmatch = ', findmatch, 'table count: ', tablenum)
            if blkcase == 15 or blkcase == 14 or blkcase == 11 or blkcase == 10 or blkcase == 7 or blkcase == 6 or blkcase == 3 or blkcase == 2:
#                print('process section 2 - tablenum', tablenum)
                nowcol = jet2 & 48
                if nowcol == 0:
                    newcol = 'yellow'
                if nowcol == 16:
                    newcol = 'green'
                if nowcol == 32:
                    newcol = 'red'
                if nowcol == 48:
                    newcol = 'gray'
            

        ## The following is used to detemine which section 2 table was updated

                lastcolor = tableallo[findmatch + 3]
                tableallo[findmatch + 3] = str(newcol)
                tableallo[findmatch + 2] = jet2
                tablenum = tablenum + 1
#                print('One just added to tablenum sec 2')
                if lastcolor != newcol:
#                    print('lastcolor != newcol sec 2')
                    ncolor = tableallo[findmatch + 3]
                    network_out(tableallo, rest_id, cus_id, tablenum, ncolor, jet1, jet2, urlx, mystring)
#                print('HERE 3')
                findmatch = findmatch + 4
#                print('4 just added to findmatch sec 2')
##       Section 1

#            print('I LOOPED PAST HERE 4 section 1! findmatch = ', findmatch, 'table count: ', tablenum, 'blkcase: ', blkcase)

            if blkcase == 15 or blkcase == 13 or blkcase == 11 or blkcase == 9 or blkcase == 7 or blkcase == 5 or blkcase == 3 or blkcase == 1:
#                print('process section 1 - tablenum', tablenum)
                nowcol = jet2 & 192
                if nowcol == 0:
                    newcol = 'yellow'
                if nowcol == 64:
                    newcol = 'green'
                if nowcol == 128:
                    newcol = 'red'
                if nowcol == 192:
                    newcol = 'gray'


            
        ## The following is used to detemine which section 1 table was updated
#        print('I LOOPED PAST HERE 5!')

                lastcolor = tableallo[findmatch + 3]
                tableallo[findmatch + 3] = str(newcol)
                tableallo[findmatch + 2] = jet2
                tablenum = tablenum + 1
#                print('One just added to tablenum sec 1')
                if lastcolor != newcol:
#                    print('lastcolor != newcol sec 2')

                    ncolor = tableallo[findmatch + 3]
                    network_out(tableallo, rest_id, cus_id, tablenum, ncolor, jet1, jet2, urlx, mystring)
#                print('HERE 4')
                findmatch = findmatch + 4
#                print('4 just added to findmatch sec 1')

#            print('findmatch at end of loop: ', findmatch, 'table count: ', tablenum, 'blkcase: ', blkcase, 'bpos: ', bpos)
        # Set up for next loop by grabbing the next msg and resetting the blkmap pointer to zero
#        print('SET BPOS to ZERO')
#        print('mystring before taking four bytes away: ', mystring)
        mystring = mystring[4:]
        if len(mystring) == 0:
#            print('EXIT - len(mystring) = 0.')
#            print('TABLEALLo: ', tableallo)
            return write_server
        # This is to erase the rest of the bytes in the mystring buffer because the are not used for this layout.
#        if bpos == len(blkmap) and jet1 == blkmap[len(blkmap) -3]:
#            print('I am leaving here 1. mystring: ', mystring)
            # The next instruction below is not need because the return write_server clears out mystring because no value is return
#            mystring = b''
#            return write_server
        bpos = 0
        # The following is to handle when the same block comes in on jet1. 
        jetfixold = jet1
        jet1, jet2 = fourM2B(mystring, jet1, jet2)
        if jetfix == jet1 and jetfix == jetfixold:
            jetfix = 0
#        print('I am leaving here 2. ')
#        tablenumsave = tablenum
#        print('BOTTOM OF LOOP - jet1 and 2 after the update at the end of the loop', jet1, jet2, 'Table No. ', tablenum, 'mystring: ', mystring)
#        print('TABLEALLo: ', tableallo)
## END OF update_col
##
##############################################################################################
##############################################################################################
##############################################################################################


def network_out(tableallo, rest_id, cus_id, tablenum, ncolor, jet1, jet2, urlx, mystring):

#    print('I LOOPED PAST HERE 3!')

        ## The following updates the color/section data to all the section records in a given block
#    for q in range(0, len(tableallo), 4):
#        if tableallo[q] == jet1:
#            tableallo[q + 2] = jet2
             
#    difnum = random.random()   
#    pickle = 0
##    print('I LOOPED PAST HERE 9!')
##   The following is the message send out retry
#    while pickle < 6:
##        print('I LOOPED PAST HERE 10!')
##        ## Below is the single line that builds the complete data message that the url is added to in the next instruction
## tabdat = '&ctn=T' + str(tablenum) + '=' + ncolor + '|%20&restaurantid=' + str(rest_id) + '&customerid=' + str(cus_id) + '&from=H&tablechanged=T' + str(tablenum) + '=' + ncolor + '&length=' + str(int(len(tableallo) / 4)) + '&t=' + str(difnum)
##    tabdat = 'ctn' + tablenum + '=' + ncolor + 'restaurantid' : rest_id + '&customerid=' + cus_id + '&from=H&tablechanged=T' + tablenum + '%3D' + ncolor + '&length=' + int(len(tableallo) / 4) + '&t=' + difnum


##    tabdat = 'ctn=T' + str(tablenum) + '%3D' + ncolor + '%7C0+&restaurantid=' + str(rest_id) + '&customerid=' + str(cus_id) + '&from=H&tablechanged=T' + str(tablenum) + '%3D' + ncolor + '&length=' + str(int(len(tableallo) / 4)) + '&t=' + str(difnum)

##    tabdat = 'ctn=T' + str(tablenum) + '&col=' + ncolor + '&restaurantid=' + str(rest_id) + '&customerid=' + str(cus_id) + '&from=H&length=' + str(int(len(tableallo) / 4))


##

##    tabdat = {'ctn=T' + tablenum + '%3D' + ncolor + '%7C0+&restaurantid=' + rest_id + '&customerid=' + cus_id + '&from=H&tablechanged=T' + tablenum + '%3D' + ncolor + '&length=' + (len(tableallo) / 4) + '&t=' + difnum}
##    print('Single table message: ', tabdat)
##

#    r = requests.post('http://192.168.1.2:8080/tableservice/table', data =tabdat)
#    r.reason

#    params = urllib.urlencode({'=ctn=T' + str(tablenum) + '%3D' + ncolor + '%7C0+&restaurantid=' + str(rest_id) + '&customerid=' + str(cus_id) + '&from=H&tablechanged=T' + str(tablenum) + '%3D' + ncolor + '&length=' + str(int(len(tableallo) / 4)) + '&t=' + str(difnum)})
#    headers = {"Content-type": "application/x-www-form-urlencode", "Accept": "text/plain"}
#    conn = httplib.HTTPConnection(10.0.0.6:8081/table)
#    conn.request("POST", "", params, headers)
#    response = conn.getresponse()
#    print response.stsus, response.reason
#    conn.close
#
#
#        tabdat = '=ctn=T' + str(tablenum) + '%3D' + ncolor + '%7C0+&restaurantid=' + str(rest_id) + '&customerid=' + str(cus_id) + '&from=H&tablechanged=T' + str(tablenum) + '%3D' + ncolor + '&length=' + str(int(len(tableallo) / 4)) + '&t=' + str(difnum)
#
#        print('tabdat: ', tabdat)
#        ## The following is the complete message that is sent to the server


##    full_url = urlx + tabdat


##    tabdat = 'ctn=T' + str(tablenum) + '&col=' + ncolor + '&restaurantid=' + str(rest_id) + '&customerid=' + str(cus_id) + '&from=H&length=' + str(int(len(tableallo) / 4))
##    print('urlx type', type(urlx), 'tabdat type', type(tabdat))
##    full_url = urlx + '?' + tabdat
##    print('Full Url ', full_url);

##    fulurlbyte1 = full_url.encode('utf-8')
##    print('full URL type: ', type(full_url), 'fulurlbyte1 type: ', type(fulurlbyte1))

#
#& 2 Test instructions below  #######################################################################################
#        print('FULURL', full_url)  ####-  this instruction for debug

##    try:
        ## The following writes the message string to the network
        #wp = urllib.request.urlopen(full_url)
##        wp = urllib.request.urlopen(urlx, data =fulurlbyte1)
##    except HTTPError as e:
##        print('Write HTTP error')
#        return write_server

##    except URLError as e:
##        print('Write URL error')
#        return write_server            
#        print('data ', data)
#    content2 = wp.read()
#        print('LOOPED PAST HERE 8!')
#    pickle = 6

    tabdat = {'ctn':'T' + str(tablenum), 'col': ncolor, 'restaurantid' : str(rest_id), 'customerid': str(cus_id), 'from': 'H', 'length': str(int(len(tableallo) / 4))}
    print('Single table message: ', tabdat)

    json_data = json.dumps(tabdat).encode('utf-8')
    request = urllib.request.Request(urlx, data=json_data,
                                 headers={'Content-Type': 'application/json'})


#    print('tabdat type: ', type(tabdat), 'tabdatbyte type: ', type(tabdatbyte))
    try:
#        wp = urllib.request.urlopen('http://192.168.1.2:8080/tableservice/tablehw', data=(tabdat))
        f = urllib.request.urlopen(request)
#       	print('json read'+wp.read())
        print('network message did get sent')
        if f.read().decode('utf-8') == False:
            print('your message was save to the server')
    except HTTPError as e:
        print('Write HTTP error', e)
        return 'NOSERVER'
    except URLError as e:
        print('Write URL error', e)
        return 'NOSERVER'
##    content2 = wp.read()  
#    if f.read().decode('utf-8') == False:
###            Below the store serial buffer content is cleared and the serial data in bit is cleared
##            ## remove the first four characters from mystring because they have been sent
####            print('MESSAGE ERASED! **********************************:')
#        print('your message was save to the server')
#        if len(mystring) != 0:
####                mystring = mystring[4:]
####                jet1, jet2 = fourM2B(mystring)
##        pickle = 1
##    else:
##        print('Your message was missed sleep .5')
            ## loop back to resend the missed message
##        pickle = pickle + 1 
##        if pickle != 6:
##            print('Network ERROR, your sent message was never received')
##            sleep(.5)
##
## END OF NETWORK_OUT
##
##############################################################################################
##############################################################################################
##############################################################################################


        
def read_server(psloop, url, tableall, tableallo, inblock, updatehex, tabchk):
    netin = ""
    try:
#        print('this is not what it looks like')
        netin = urllib.request.urlopen(url, data=None, timeout=1).read()  # Opens link to server to read in the control file from the server
#        print('What is it? No.1')
    except HTTPError as e:
        print('Read HTTP error', e)
        return 'NOSERVER'

    except URLError as e:
        print('Read URL error', e)
        return 'NOSERVER'

    except Exception:
        print('General Exception error', e)
        return 'NOSERVER'

#    print('What is it? No.2')
    instr = netin.decode(encoding = 'utf-8')  ## This does the conversion from bytes to byte string
    ## DEBUG CODE
#    instr = instr + 'RST=1'
#    print('print instr the message from the internet', instr)

    ck4rst = ''
    ## Check to see if RST=1 is at the end of the message
    ## What are the last 5 characters in the string
#    print('Lenght of instr:', len(instr))

    if len(instr) > 6:
        for num in range((len(instr) - 5), (len(instr) - 0), 1):
            ck4rst = ck4rst + instr[num]
#            print('loop cnt', num, 'check for reset value', ck4rst)
#    if psloop == 'zero':
#        print('print complete ck4rst message: ', ck4rst)
#        print('print instr  message: ', instr)
    ## END DEBUG CODE


    ## Here is the test to see if the UPDATE command was received
    if ck4rst == 'RTS=1':

    ## Remove that 5 character message plus a space off the end of the string
        instr = instr[:-6]

    ## The following sends the table all image to the TC network        
        for q in range(0, len(tableall), 4):
            inblock = tableall[q] 
            updatehex = tableall[q + 2]
            sendmess(inblock, updatehex)

    # The next function removes the data between the '|' and '~'.
    addvar = 1
    yin = ''
    for ch in instr:
        if ch == '|':
            addvar = 0
            yin = yin + ch
        if ch == '~':
            addvar = 1
        if addvar == 1:
            yin = yin + ch

##    print('y: ', yin)

    yin = re.sub(r'[|]', '', yin)  #remove all the pipes from the string
    yin = re.sub(r'[~]', ' ', yin)  #remoe all the '~' from the string
    yin = re.sub(r'["]', '', yin)   
   ##yin = yin[:-1]                  #remove the last character from the string
    yin = re.sub(r'[=]', ' ', yin) # Using the equal sign, we add a space for


    listready = yin.split()  # Split ['T1 green'] into records, 'T1' and 'green'

#    print('Here is listready 1st time: ', listready)

#    listready = listready[3:]
#    listready = re.sub(r'["]', '', listready)

#    print('Here is listready 2nd time: ', listready)



			## We need to check the serial port for activity
#    tabchk = ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12', 'T13', 'T14', 'T15', 'T16', 'T17', 'T18', \
#              'T19', 'T20', 'T21', 'T22', 'T23', 'T24', 'T25', 'T26', 'T27', 'T28', 'T29', 'T30', 'T31', 'T32', 'T33', 'T34', \
#              'T35', 'T36', 'T37', 'T38', 'T39', 'T40', 'T41', 'T42', 'T43', 'T44', 'T45', 'T46', 'T47', 'T48', 'T49', 'T50', \
#              'T51', 'T52', 'T53', 'T54', 'T55', 'T56', 'T57', 'T58', 'T59', 'T60', 'T61', 'T62', 'T63', 'T64', 'T65', 'T66', \
#              'T67', 'T68', 'T69', 'T70', 'T71', 'T72', 'T73', 'T74', 'T75', 'T76', 'T77', 'T78', 'T79', 'T80', 'T81', 'T82', \
#              'T83', 'T84', 'T85', 'T86', 'T87', 'T88', 'T89', 'T90', 'T91', 'T92', 'T93', 'T94', 'T95', 'T96', 'T97', 'T98', \
#              'T99', 'T100', 'T101', 'T102', 'T103', 'T104', 'T105', 'T106', 'T107', 'T108', 'T109', 'T110', 'T111', \
#              'T112', 'T113', 'T114', 'T115', 'T116', 'T117', 'T118', 'T119', 'T120', 'T121', 'T122', 'T123', 'T124', \
#              'T125', 'T126', 'T127', 'T128', 'T129', 'T130', 'T131', 'T132' , 'T133', 'T134', 'T135', 'T136', 'T137', \
#              'T138', 'T139', 'T140', 'T141', 'T142', 'T143', 'T144', 'T145', 'T146', 'T147', 'T148', 'T149', 'T150', \
#              'T151', 'T152', 'T153', 'T154', 'T155', 'T156', 'T157', 'T158', 'T159', 'T160', 'T161', 'T162', 'T163', 'T164', 'T165']





    if len(listready) != (len(tabchk) * 2):
        print('ERROR  ERROR  ERROR  ERROR')
        print('Incoming message length does not match the number of tables for this restuarant')
        print('lenght of listready: ', len(listready), 'lenght of tabchk', len(tabchk))
        if len(listready) == 1:
            print('Since listready lenght = 1, most likely the local server has an empty tableStat file')    

    else:
        for tb in range(0, len(tabchk), 1):
            if tabchk[tb] != listready[ tb * 2 ]:
                print('ERROR  ERROR  ERROR  ERROR')
                print('Table position', (tb + 1 ), 'from the server does not match the verify position list.')

    changes = 0
    newtable = '0'
    chgsect = '0'
    ncolor = 'pink'
#    inblock = '0x0'
    curhexval = '0x0'
#    updatehex = '0x0'
#    print('List Ready: ', listready)
    ##print('Instr: ', instr)
    num = 0
    numsave = 0
##    go1 = tableall[(num * 2) + 3]  ## Value of current panel colors
#    print('GO1 ..', go1)
#    print('psloop before listready check 3', psloop)
    if (len(listready) * 2) > len(tableall):
#        print("list ready lenght error - ", "listready lenght: ", len(listready))
        print('ERROR: Listready lenght ', len(listready), 'num value larger than listready lenght most likely local server needs reboot: ')
        return read_server
##    go2 = listready[num] ## changes of colors coming from server
##    print('Listready lenght ', len(listready))
    for num in range(0, len(listready), 2):
        go1 = tableall[int((num * 2) + 3)]  ##  Value from copy of current panel status
        go2 = listready[num + 1]  ## Value from server
        if go1 != go2:  #  Compare the old colors to new color, if they don't
                        # match, then you have found the one that changed
#            sleep(0.07)  # short delay for TableCheck Network
            newtable = listready[num]  #  new Table number with 'T' like T3
            ncolor = listready[(num + 1)]  # Grab that new color from the incoming message
#            print('NCOLOR: ', ncolor, type(ncolor))
            changes = changes + 1  # number of changes from old network record to new network
            chgsect = tableall[(num * 2) + 1]  #Collect the current section info to determine
                                                #which leds changed  and what bit mask to use
            inblock = tableall[(num * 2)]  # Collect inblock for writing message to panel
#            print('INBLOCK: ', inblock, type(inblock), 'num: ', num)
            numsave = num            
            curhexval = tableall[(numsave * 2) + 2]  # Collect the4 current LED color and section data
#            print("New Table: ", newtable, "Section: ", chgsect, "New color: ", ncolor)    
            newcmask = 255

            ##

##            print('curhexval fresh from the table: ', curhexval, 'curhexval type: ', type(curhexval))
            ## Determine below which section changed and what bit mask to use
            if chgsect == 1:

                if ncolor == 'yellow':
                    newcmask = 0
                if ncolor == 'green':
                    newcmask = 64
                if ncolor == 'red':
                    newcmask = 128
                if ncolor == 'gray':
                    newcmask = 192
                    
                curhexmask = curhexval & 63  #Maskoff section bits
                updatehex = curhexmask | newcmask #Or together the color bits into the section

            if chgsect == 2:

                if ncolor == 'yellow':
                    newcmask = 0
                if ncolor == 'green':
                    newcmask = 16
                if ncolor == 'red':
                    newcmask = 32
                if ncolor == 'gray':
                    newcmask = 48
                
                curhexmask = curhexval & 207  #Maskoff section bits
                updatehex = curhexmask | newcmask #Or together the color bits into the section

            if chgsect == 3:

                if ncolor == 'yellow':
                    newcmask = 0
                if ncolor == 'green':
                    newcmask = 4
                if ncolor == 'red':
                    newcmask = 8
                if ncolor == 'gray':
                    newcmask = 12

                curhexmask = curhexval & 243  #Maskoff section bits
                updatehex = curhexmask | newcmask #Or together the color bits into the section

            if chgsect == 4:

                if ncolor == 'yellow':
                    newcmask = 0
                if ncolor == 'green':
                    newcmask = 1
                if ncolor == 'red':
                    newcmask = 2
                if ncolor == 'gray':
                    newcmask = 3
               
                curhexmask = curhexval & 252  #Maskoff section bits
                updatehx = curhexmask | newcmask #Or together the color bits into the section
                updatehex = int(updatehx)  # This is the rebuilt LED color and section byte
##            tableall[(numsave * 2) + 2] = updatehex  # Update the color section value for this block

            ## The following updates the color/section data to all the block records
            for k in range(0, len(tableall), 4):
                if tableall[k] == inblock:
                    tableall[k + 2] = updatehex
                    tableallo[k + 2] = updatehex

             ###########################
             # SAVE THE NEW COLOR AND HEXVALUE TO THE TABLE
             ##########################

#            print('NCOLOR.. The new color', ncolor)
#            print('inblock value and type: ', hex(inblock), type(inblock))
#            print('newcmask value and type: ', newcmask, type(newcmask))
#            print('updatehex going to the status buffer value and type: ', updatehex, type(updatehex))
            tableall[int((numsave * 2) + 3)] = ncolor  ## Save the new color to the copy of the last netwrk message
#            print('Value written back into the buffer that tracks currect panel state. ', str('0x' + '%.2x' % updatehex))
            tableall[int((numsave * 2) + 2)] = updatehex  ## This takes the value in decimal converts to hex
                                                                                    ##and adds 0x to front of value.  The other side of
                                                                                    ##the equal sign is putting the value into tableall.count 
#            print('What did I put in tableall? ', tableall[int((numsave * 2) + 2)])
            ##########################
            # Encode the two bytes into four
            #########################
            sendmess(inblock, updatehex)
#        else:
#            print('go1 = go2.')

#    print('psloop at end of read_server 4', psloop)
#    return psloop
########
######## END of readserver


def sendmess(inblock, updatehex):
            ########################
            # TAKE TWO BYTES CONVERT TO FOUR TRANSMISSION BYTES
            ########################

            ##below the two bytes to be sent are split into four bytes with all data in the lower nibble
            zonk1 = inblock & 15  # Mask off upper nibble  
            zonk2 = (inblock >> 4) & 15  #mask off lower nibble and rotate upper into lower position
            zonk3 = updatehex & 15  # Mask off upper nibble
            zonk4 = (updatehex >> 4) & 15  #mask off lower nibble and rotate upper into lower position
#            print('Zonk 3 Znok 3 Zonk 3 ', zonk3)

            ##  add 0x10, 0x20 and 0x30 to the values
            zonk2 = zonk2 + 16    
            zonk3 = zonk3 + 32
            zonk4 = zonk4 + 48

#            print('FIRST zonk1, 2, 3 and 4: ', zonk1, zonk2, zonk3, zonk4)
            
            # The following converts intergers to hex like 18 decimal to 12 hex
            zonk1 = str('%.2x' % zonk1)
            zonk2 = str('%.2x' % zonk2)
            zonk3 = str('%.2x' % zonk3)
            zonk4 = str('%.2x' % zonk4)

#            print('zonk1, 2, 3 and 4: ', zonk1, zonk2, zonk3, zonk4)
            
        ##    print('ZONK!&&& ', zonk1)
            # The following converts the hex values from above to hex strings, like hex 12 to '12'
            zonk66 = [(zonk1), (zonk2), (zonk3), (zonk4)]
#            print('zonk66', zonk66)
            # The following converts ['07', '12', '25', '39'] to b'\x07\x12%9' 
            jim = bytes.fromhex(zonk66[0]) + bytes.fromhex(zonk66[1]) + bytes.fromhex(zonk66[2]) + bytes.fromhex(zonk66[3])
#            print('This is good old Jim: ', jim, 'inblock and type: ', inblock, type(inblock), 'updatehex and type: ', updatehex, type(updatehex))
            # Write the date out the serial port
            chk = ser.write(jim)
            sleep(0.07)  # short delay for TableCheck Network
########
######## END of sendmess


def fourM2B(mystring, jet1, jet2):
#        print('Loop mystring data: ', mystring, len(mystring))
        if len(mystring) > 3:   # check to prevent "index out of range" error
            bld0 = mystring[0]
            bld1 = mystring[1]
            bld2 = mystring[2]
            bld3 = mystring[3]

        # Below is where we subtract out the 0, 10, 20, and 30 hex values from the first half of
        # the panel bytes to remove the message sequence numbers from the bytes
            bld1 = bld1 - 16
            bld2 = bld2 - 32
            bld3 = bld3 - 48


                    ## Take the lower half of the first two bytes to construct the table block
            jet1 = (bld1 * 16) + bld0

                    ## Take the lower half of the second two bytes and combine together for LED status
            jet2 = (bld3 * 16) + bld2

        else:
            print('fourM2M ERROR: mystring was not long enough to convert 4 bytes of data')
        return (jet1, jet2)

########
######## END of fourM2B
#
#
#
def ipcheck(pop):
    status,result = subprocess.getstatusoutput("ping -c 1 " + str(pop))
    print (pop, status,result)
    if status == 0:
        return "UP"
    else:
        return "DOWN"

def getServerIp():
  global server_ip
  global threadCnt
  global threadOver
  s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
  try:
   # doesn't even have to be reachable
   s.connect(('10.255.255.255', 1))
   local_ip = s.getsockname()[0]
  except:
   local_ip = '127.0.0.1'
  finally:
   s.close()
  print (local_ip)

  #local_ip = socket.gethostbyname(socket.gethostname())
  #local_ip = socket.gethostbyname_ex(socket.gethostname())[2][0];
  local_ips = local_ip.split('.')
  local_ip = local_ips[0] +'.'+ local_ips[1] +'.'+ local_ips[2]+'.';
  #threads = []
  if len(server_ip) == 0:
   for num in range(255):
    if len(server_ip) == 0:
     ip = ''+local_ip+str(num)
     th = threading.Thread(target=scanServerIp, args=(ip,))
     th.start();
     #threads.append(th)
     #threadCnt =+1
     print('Thread starts to ping ',ip)
   threadOver = 1
   time.sleep(5)
   if len(server_ip) == 0:
    time.sleep(20)

def scanServerIp(ip):
  global server_ip
  if len(server_ip) == 0:
   resp = ipcheck(ip)
   print(ip, ' is ', resp)
   if resp == "UP":
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(2)                                      #2 Second Timeout
    result = sock.connect_ex((ip,8080))
    if result == 0:
      print ('Server Port OPEN on ', ip)
      server_ip = ip
      return ip
                

#while 1 == 1:
main()
#	time.sleep(30)

