import os

import board
import neopixel
import sys
import time

file_name = 'light-show.txt'
if len(sys.argv) > 1:
    file_name = sys.argv[1]
print(f"Use and monitor {file_name}\n")
file_hash = 0
file_timestamp = 0

# array of arrays or tuples
sequence: [[tuple]] = []
last_check = 0
current_line: int = 0
frame_length = 0.02

ORDER = neopixel.GRB

pixels = neopixel.NeoPixel(board.D18, 300, brightness=1, auto_write=False, pixel_order=ORDER)
pixels.fill((0, 0, 0))


def draw_line(line_n: int) -> int:
    global sequence
    led_string_state = sequence[line_n]

    for led_num in range(len(led_string_state)):
        try:
            led_state = led_string_state[led_num]
            if ORDER == neopixel.GRB:
                pixels[led_num] = (led_state[1], led_state[0], led_state[2])
            else:
                pixels[led_num] = led_state
        except IndexError:
            print(f"IndexError: led_num: {led_num}, led_string_state: {led_string_state}")
    pixels.show()
    if line_n == len(sequence) - 1:
        return 0
    else:
        return line_n + 1


def read_line(line: str) -> [tuple]:
    led_colors_def: [] = str.split(line, ',')
    led_num = 0
    led_colors = []
    for triplet in led_colors_def:
        r, g, b = str.split(triplet, ' ')
        # print(f"led_num: {led_num}, r: {r}, g: {g}, b: {b}")
        led_num += 1
        triplet = (int(r), int(g), int(b))
        led_colors.append(triplet)
    return led_colors


def read_show_file():
    global file_name
    global sequence
    print(f"Reading {file_name}")
    we_have_written_END = False
    while not we_have_written_END:
        with open(file_name, "r") as f:
            x = f.read()
            if x.find("END") != -1:
                we_have_written_END = True
            else:
                time.sleep(0.1)

    last_file_size = os.path.getsize(file_name)
    size_changes = True
    while size_changes:
        time.sleep(0.3)
        current_file_size = os.path.getsize(file_name)
        size_changes = current_file_size != last_file_size
        last_file_size = current_file_size
    with open(file_name, 'r') as f:
        lines = f.readlines()
        new_sequence = []
        for line in lines:
            if line.find("END") == -1:
                new_sequence.append(read_line(line))
    sequence = new_sequence


def load_file_if_changed() -> bool:
    global file_name
    global file_timestamp
    current_file_timestamp = os.path.getmtime(file_name)
    if current_file_timestamp != file_timestamp:
        read_show_file()
        file_timestamp = current_file_timestamp
        return True
    return False


while True:
    if time.time() - last_check > 5:
        if load_file_if_changed():
            current_line = 0
    current_line = draw_line(current_line)
    time.sleep(frame_length)
