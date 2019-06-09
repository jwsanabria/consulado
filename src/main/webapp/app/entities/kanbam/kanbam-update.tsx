import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './kanbam.reducer';
import { IKanbam } from 'app/shared/model/kanbam.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IKanbamUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IKanbamUpdateState {
  isNew: boolean;
}

export class KanbamUpdate extends React.Component<IKanbamUpdateProps, IKanbamUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { kanbamEntity } = this.props;
      const entity = {
        ...kanbamEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/kanbam');
  };

  render() {
    const { kanbamEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="miconsuladogatewayApp.kanbam.home.createOrEditLabel">Create or edit a Kanbam</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : kanbamEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="kanbam-id">ID</Label>
                    <AvInput id="kanbam-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="sizeLabel" for="kanbam-size">
                    Size
                  </Label>
                  <AvField id="kanbam-size" type="string" className="form-control" name="size" />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel" for="kanbam-state">
                    State
                  </Label>
                  <AvInput
                    id="kanbam-state"
                    type="select"
                    className="form-control"
                    name="state"
                    value={(!isNew && kanbamEntity.state) || 'TO_DO'}
                  >
                    <option value="TO_DO">TO_DO</option>
                    <option value="DOING">DOING</option>
                    <option value="DONE">DONE</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/kanbam" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  kanbamEntity: storeState.kanbam.entity,
  loading: storeState.kanbam.loading,
  updating: storeState.kanbam.updating,
  updateSuccess: storeState.kanbam.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(KanbamUpdate);
