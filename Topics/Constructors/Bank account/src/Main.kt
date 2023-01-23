// write the BankAccount class here
class BankAccount(deposited: Long, withdrawn: Long) {
    var deposited: Long = deposited
    var withdrawn: Long = withdrawn
    var balance: Long = deposited - withdrawn
}