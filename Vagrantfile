# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.define "db" do |db|
    db.vm.box = "sohoffice/centos-7-postgres-10"
    db.vm.hostname = "db.slick-postgis-demo.vm"
    db.vm.box_version = ">= 0.0.2"

    db.vm.provider :virtualbox do |vb|
      vb.customize ["modifyvm", :id, "--ioapic", "on"]
      vb.customize ["modifyvm", :id, "--memory", "2048"]
      vb.customize ["modifyvm", :id, "--cpus", "2"]
    end

    db.vm.network :private_network, ip: "192.168.56.98"
  end

end
