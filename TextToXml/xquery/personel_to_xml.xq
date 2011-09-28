(:: pragma bea:mfl-element-parameter parameter="$personel1" type="Personel@" location="../mfl/personel.mfl" ::)
(:: pragma bea:global-element-return element="root" location="../xsd/personel.xsd" ::)

declare namespace xf = "http://tempuri.org/MHS/xquery/personel_to_xml/";

declare function xf:personel_to_xml($personel1 as element())
    as element(root) {
        let $Personel := $personel1
        return
            <root>
                {
                    for $Customer in $Personel/Customer
                    return
                        <customer>
                            <customer_id>{ data($Customer/CustomerId) }</customer_id>
                            <lastname>{ data($Customer/Lastname) }</lastname>
                            <firstname>{ data($Customer/Firstname) }</firstname>
                            {
                                for $Account in $Customer/Account
                                return
                                    <account>
                                        <account_id>{ data($Account/AccountT) }</account_id>
                                        {
                                            for $Transactions in $Account/Transactions
                                            return
                                                <transaction>
                                                    <label>{ data($Transactions/Description) }</label>
                                                    <value>{ data($Transactions/Amount) }</value>
                                                </transaction>
                                        }
                                    </account>
                            }
                        </customer>
                }
            </root>
};

declare variable $personel1 as element() external;

xf:personel_to_xml($personel1)