import zlib

# Replace 'data' with the bytes of the data you want to checksum
data = b""

# Calculate CRC32
checksum = zlib.crc32(data) & 0xFFFFFFFF  # Mask to 32-bits
print(f"CRC32 Checksum: {checksum:08X}")