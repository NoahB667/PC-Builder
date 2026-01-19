import pypartpicker
import asyncio

async def get_parts(part_id):
    async with pypartpicker.AsyncClient() as pcpp:
        part = await pcpp.get_part(f"https://pcpartpicker.com/product/{part_id}")

    for spec, value in part.specs.items():
        print(f"{spec}: {value}")

asyncio.run(get_parts("fPyH99"))
