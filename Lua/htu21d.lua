htu21d = {
  dev_addr = 0x40,
  id = 0,
  init = function (self, sda, scl)
           self.id = 0
           i2c.setup(self.id, sda, scl, i2c.SLOW)
  end,
  read_reg = function(self, dev_addr, reg_addr)
      i2c.start(self.id)
      i2c.address(self.id, dev_addr, i2c.TRANSMITTER)
      i2c.write(self.id, reg_addr)
      --i2c.stop(self.id)
      i2c.start(self.id)
      i2c.address(self.id, dev_addr, i2c.RECEIVER)
      tmr.delay(50000) --wait for measurment
      c = i2c.read(self.id, 2)
      i2c.stop(self.id)
      return c
  end,
  readTemp = function(self)
      h, l = string.byte(self:read_reg(0x40, 0xE3), 1, 2)
      h1=bit.lshift(h,8)
      rawtemp = bit.band(bit.bor(h1,l),0xfffc)
      --h1=bit.lshift(h,8) | l
      --rawtemp &= 0xfffc
      temp = ((rawtemp/65536.0)*175.72)-46.85
	  --temp = ((rawtemp/65536)*176)-47
      return temp
  end,
  readHum = function(self)
      h,l = string.byte(self:read_reg(0x40,0xE5),1,2)
      h1=bit.lshift(h,8)
      rawhum = bit.band(bit.bor(h1,1),0xfffc)
      hum = ((rawhum/65536.0)*125.0)-6.0
	  --hum = ((rawhum/65536)*125)-6
      return hum
  end,
  find_dev = function(self)
     i2c.start(self.id)
     c = i2c.address(self.id, self.dev_addr, i2c.TRANSMITTER)
     i2c.stop(self.id)
     return c
  end
} 